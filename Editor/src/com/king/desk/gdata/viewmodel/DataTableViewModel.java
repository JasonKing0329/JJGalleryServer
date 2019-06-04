package com.king.desk.gdata.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.king.desk.gdata.Conf;
import com.king.desk.gdata.Constants;
import com.king.desk.gdata.Constants.CommonColumn;
import com.king.desk.gdata.model.FileUtil;
import com.king.desk.gdata.model.SqlInstance;
import com.king.desk.gdata.model.TableComparator;
import com.king.desk.gdata.model.TableModel;
import com.king.desk.gdata.model.bean.ColumnBean;
import com.king.desk.gdata.model.bean.SaveData;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.desk.gdata.model.live.LiveData;
import com.king.desk.gdata.model.live.Observable;
import com.king.desk.gdata.model.live.ObservableEmitter;
import com.king.desk.gdata.model.live.ObservableOnSubscribe;
import com.king.desk.gdata.model.live.Observer;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordStar;
import com.king.service.gdb.bean.RecordType1v1;
import com.king.service.gdb.bean.RecordType3w;

public class DataTableViewModel {

	public LiveData<String> messageObserver = new LiveData<>();
	public LiveData<Boolean> loadingObserver = new LiveData<>();
	
	public LiveData<List<TableItem>> tableDataObserver = new LiveData<>();
	public LiveData<List<TableItem>> filterDataObserver = new LiveData<>();
	public LiveData<Boolean> fileChangeObserver = new LiveData<>();
	public LiveData<Boolean> tableUpdateObserver = new LiveData<>();
	public LiveData<Boolean> saveSuccessObserver = new LiveData<>();

	private List<TableItem> mCurrentList;
	
	private String mTableType;
	
	private TableModel tableModel;
	
	private List<ColumnBean> columnList;
	
	private int[] starRelatedPosition;

	private int[] detailRelatedPosition;
	
	private SaveData mSaveData;
	
	private CommonColumn mSortColumn = CommonColumn.SEQ;
	
	private boolean mDesc;
	
	public DataTableViewModel() {
		mSaveData = new SaveData();
		tableModel = new TableModel();
		starRelatedPosition = new int[] {
				CommonColumn.TOP.ordinal(),
				CommonColumn.BOTTOM.ordinal(),
				CommonColumn.MIX.ordinal(),
				CommonColumn.SCORE_TOP.ordinal(),
				CommonColumn.SCORE_BOTTOM.ordinal(),
				CommonColumn.SCORE_MIX.ordinal(),
				CommonColumn.SCORE_C_TOP.ordinal(),
				CommonColumn.SCORE_C_BOTTOM.ordinal(),
				CommonColumn.SCORE_C_MIX.ordinal()
		};
		detailRelatedPosition = new int[] {
				CommonColumn.TYPE.ordinal(),
				CommonColumn.FK.ordinal(),
				CommonColumn.OTHER.ordinal()
		};
	}
	
	public String[] getSource() {
		return Constants.source;
	}
	
	public String getSelectedSource(int position) {
		return Constants.source[position];
	}

	public void loadTableData(final String type) {
		mTableType = type;
		loadingObserver.setValue(true);
		Observable.create(new ObservableOnSubscribe<List<TableItem>>() {

			@Override
			public void subscribe(
					ObservableEmitter<List<TableItem>> e) 
							throws Exception {

				tableModel.loadStarMap();
				
				columnList = tableModel.createColumnList(type);
				
				List<Record> list = tableModel.queryAllRecords();
				
				List<TableItem> tableItems = toViewItems(list);
				
				e.onNext(tableItems);
			}

		}).subscribe(new Observer<List<TableItem>>() {
			
			@Override
			public void onNext(List<TableItem> value) {
				mCurrentList = value;
				loadingObserver.setValue(false);
				tableDataObserver.setValue(value);
			}

			@Override
			public void onError(Throwable e) {
				loadingObserver.setValue(false);
				messageObserver.setValue(e.getMessage());
			}
		});
	}

	protected List<TableItem> toViewItems(List<Record> list) {
		List<TableItem> items = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Record record = list.get(i);
			TableItem item = new TableItem();
			item.setIndexInTotalList(i);
			if (record.getType() == GDBProperites.VALUE_RECORD_TYPE_1V1) {
				RecordType1v1 data = tableModel.loadRecordType1v1(record.getRecordDetailId());
				item.setDetail(data);
			}
			else {
				RecordType3w data = tableModel.loadRecordType3w(record.getRecordDetailId());
				item.setDetail(data);
			}
			item.setBean(record);
			List<RecordStar> stars = tableModel.getRecordStars(record.getId());
			item.setStars(stars);
			tableModel.formatDate(item);
			tableModel.formatStarString(item);
			items.add(item);
		}
		return items;
	}
	
	public void refreshStarInfo(TableItem item) {
		tableModel.formatStarString(item);
	}

	public List<ColumnBean> getColumnList() {
		return columnList;
	}
	
	public void setVersion(String version) {
		mSaveData.setVersion(version);
	}

	public void setDeleteStarWithoutRecords(Boolean delete) {
		mSaveData.setDeleteStarWithoutRecords(delete);
	}

	public void save() {
		loadingObserver.setValue(true);
		Observable.create(new ObservableOnSubscribe<Boolean>() {

			@Override
			public void subscribe(ObservableEmitter<Boolean> e)
					throws Exception {
				
				// 保存不受当前界面过滤影响，肯定从全部中操作
				mSaveData.setList(tableDataObserver.getValue());
				
				tableModel.saveTable(mSaveData);
				e.onNext(true);
			}
		}).subscribe(new Observer<Boolean>() {

			@Override
			public void onNext(Boolean value) {
				loadingObserver.setValue(false);
				setFileChanged(false);
				saveSuccessObserver.setValue(true);
				// 重新加载数据
				loadTableData(mTableType);
				messageObserver.setValue("保存成功");
				// 保存成功后自动备份数据库
				backupDatabase();
			}

			@Override
			public void onError(Throwable e) {
				loadingObserver.setValue(false);
				messageObserver.setValue(e.getMessage());
			}
		});
	}
	
	public TableItem getTableItem(int row) {
		return mCurrentList.get(row);
	}

	public void deleteItem(int row) {
		TableItem item = mCurrentList.get(row);
		// 新增记录直接删除
		if (item.getBean().getId() == null) {
			deleteNewRecord(row);
		}
		// 已有记录标记待删除
		else {
			setFileChanged(true);
			item.setBasicChanged(true);
			item.setDeleted(true);
		}
	}

	private void deleteNewRecord(int row) {
		mCurrentList.remove(row);
		// 如果当前是处于过滤列表中，还要在原列表中删除
		if (mCurrentList != tableDataObserver.getValue()) {
			for (int i = 0; i < tableDataObserver.getValue().size(); i++) {
				if (tableDataObserver.getValue().get(i).getIndexInTotalList() == mCurrentList.get(row).getIndexInTotalList()) {
					tableDataObserver.getValue().remove(i);
					break;
				}
			}
		}
	}

	public void setFileChanged(boolean changed) {
		fileChangeObserver.setValue(changed);
	}

	public boolean isFileChanged() {
		Boolean changed = fileChangeObserver.getValue();
		return changed == null ? false:changed;
	}

	public void changeItemValue(String value, int row, int col) {
		TableItem item = mCurrentList.get(row);
		tableModel.updateTableItem(item, col, value);
	}

	public void destroy() {
		SqlInstance.get().close();
	}

	public boolean isStarRelatedCell(int column) {
		for (int i = 0; i < starRelatedPosition.length; i++) {
			if (column == starRelatedPosition[i]) {
				return true;
			}
		}
		return false;
	}

	public boolean isDetailRelatedCell(int column) {
		for (int i = 0; i < detailRelatedPosition.length; i++) {
			if (column == detailRelatedPosition[i]) {
				return true;
			}
		}
		return false;
	}

	public void newRecord() {
		TableItem item = new TableItem();
		item.setBasicChanged(true);
		item.setDetailChanged(true);
		item.setStarChanged(true);
		item.setBean(new Record());
		mCurrentList.add(item);
		setFileChanged(true);
		tableUpdateObserver.setValue(true);
		// 当前界面是过滤的列表，还要加在原列表中
		if (mCurrentList != tableDataObserver.getValue()) {
			tableDataObserver.getValue().add(item);
		}
	}

	public void readFileDate(int row) {
		TableItem item = mCurrentList.get(row);
		long time = tableModel.readRecordModifyTime(item);
		if (time == 0) {
			messageObserver.setValue(item.getBean().getDirectory() + "/" + item.getBean().getName() + " 不存在");
		}
		else {
			if (item.getBean().getLastModifyTime() != time) {
				setFileChanged(true);
				item.setBasicChanged(true);
				item.getBean().setLastModifyTime(time);
				tableModel.formatDate(item);
				tableUpdateObserver.setValue(true);
			}
		}
	}

	public String getVersion() {
		return tableModel.getVersion();
	}

	public void searchTable(String text) {
		if (text == null || text.length() == 0) {
			mCurrentList = tableDataObserver.getValue();
		}
		else {
			List<TableItem> list = new ArrayList<>();
			for (TableItem item : tableDataObserver.getValue()) {
				String name = item.getBean().getName();
				if (name != null && name.toLowerCase().contains(text.toLowerCase())) {
					list.add(item);
					continue;
				}
				String top = item.getTop();
				if (top != null && top.toLowerCase().contains(text.toLowerCase())) {
					list.add(item);
					continue;
				}
				String bottom = item.getBottom();
				if (bottom != null && bottom.toLowerCase().contains(text.toLowerCase())) {
					list.add(item);
					continue;
				}
				String mix = item.getMix();
				if (mix != null && mix.toLowerCase().contains(text.toLowerCase())) {
					list.add(item);
					continue;
				}
			}
			mCurrentList = list;
		}
		filterDataObserver.setValue(mCurrentList);
	}

	public void filterByType(int type) {
		// 全部
		if (type == 0) {
			mCurrentList = tableDataObserver.getValue();
		}
		else {
			List<TableItem> list = new ArrayList<>();
			for (TableItem item : tableDataObserver.getValue()) {
				if (item.getBean().getType() == type) {
					list.add(item);
				}
			}
			mCurrentList = list;
		}
		filterDataObserver.setValue(mCurrentList);
	}

	public void filterModify(boolean isModify) {
		// 全部
		if (isModify) {
			List<TableItem> list = new ArrayList<>();
			for (TableItem item : tableDataObserver.getValue()) {
				if (item.isBasicChanged() || item.isDeleted() || item.isStarChanged() || item.isDetailChanged()) {
					list.add(item);
				}
			}
			mCurrentList = list;
		}
		else {
			mCurrentList = tableDataObserver.getValue();
		}
		filterDataObserver.setValue(mCurrentList);
	}

	/**
	 * 备份当前数据库到FILE_DB目录下
	 */
	private void backupDatabase() {
		Observable.create(new ObservableOnSubscribe<Boolean>() {

			@Override
			public void subscribe(ObservableEmitter<Boolean> e)
					throws Exception {
				FileUtil.copyFile(Conf.WEB_DB, Conf.FILE_DB);
			}
		}).subscribe(new Observer<Boolean>() {

			@Override
			public void onNext(Boolean value) {
				
			}

			@Override
			public void onError(Throwable e) {
				messageObserver.setValue("备份数据库失败：" + e.getMessage());
			}
		});
	}

	/**
	 * 备份到历史目录下
	 */
	public void backupHistoryDatabase() {
		Observable.create(new ObservableOnSubscribe<Boolean>() {

			@Override
			public void subscribe(ObservableEmitter<Boolean> e)
					throws Exception {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String target = Conf.HISTORY_DIR + "/gdata_" + sdf.format(new Date(System.currentTimeMillis())) + ".db";
				FileUtil.copyFile(Conf.WEB_DB, target);
			}
		}).subscribe(new Observer<Boolean>() {

			@Override
			public void onNext(Boolean value) {
				
			}

			@Override
			public void onError(Throwable e) {
				messageObserver.setValue("备份数据库失败：" + e.getMessage());
			}
		});
	}

	public void sortRecords(CommonColumn column) {
		if (column != mSortColumn) {
			mDesc = !mDesc;
		}
		Collections.sort(mCurrentList, new TableComparator(column, mDesc));
		filterDataObserver.setValue(mCurrentList);
	}

}
