package com.king.desk.gdata.adapter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.king.desk.gdata.Constants.CommonColumn;
import com.king.desk.gdata.model.bean.ColumnBean;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.desk.gdata.view.OnDataChangedListener;

public class TableAdapter extends AbstractTableModel {

	private List<ColumnBean> columnList;
	
	private List<TableItem> tableItemList;
	
	private OnDataChangedListener onDataChangedListener;
	
	private int[] disableEditPosition;
	
	public TableAdapter() {
		disableEditPosition = new int[] {
				CommonColumn.SEQ.ordinal(),// 序号
				CommonColumn.DATE.ordinal(),// 日期
				// 与star有关的打开新的对话框编辑，也不允许在表格编辑
				CommonColumn.TOP.ordinal(),
				CommonColumn.BOTTOM.ordinal(),
				CommonColumn.MIX.ordinal(),
				CommonColumn.SCORE_TOP.ordinal(),
				CommonColumn.SCORE_BOTTOM.ordinal(),
				CommonColumn.SCORE_MIX.ordinal(),
				CommonColumn.SCORE_C_TOP.ordinal(),
				CommonColumn.SCORE_C_BOTTOM.ordinal(),
				CommonColumn.SCORE_C_MIX.ordinal(),
				// 与detail有关的打开新的对话框编辑，也不允许在表格编辑
				CommonColumn.TYPE.ordinal(),
				CommonColumn.FK.ordinal(),
				CommonColumn.OTHER.ordinal(),
				// lastModifyTime从文件中读取，不允许编辑
				CommonColumn.DATE.ordinal()
		};
	}
	
	public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
		this.onDataChangedListener = onDataChangedListener;
	}

	@Override
	public int getColumnCount() {

		return columnList == null ? 0:columnList.size();
	}

	@Override
	public String getColumnName(int index) {
		return columnList.get(index).getName();
	}

	@Override
	public int getRowCount() {

		return tableItemList == null ? 0:tableItemList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == CommonColumn.SEQ.ordinal()) {// 序号
			return String.valueOf(row + 1);
		}
		else {
			TableItem item = tableItemList.get(row);
			return item.getValueAt(col);
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		for (int i = 0; i < disableEditPosition.length; i++) {
			if (col == disableEditPosition[i]) {
				return false;
			}
		}
		return true;
	}
	

	@Override
	public void setValueAt(Object value, int row, int col) {
		String strValue = value.toString();
		TableItem item = tableItemList.get(row);
		if (!strValue.equals(item.getValueAt(col))) {
			onDataChangedListener.onDataChanged(value.toString(), row, col);
		}
	}

	public void setColumnList(List<ColumnBean> columnList) {
		this.columnList = columnList;
	}

	public List<TableItem> getTableItemList() {
		return tableItemList;
	}

	public void setTableItemList(List<TableItem> tableItemList) {
		this.tableItemList = tableItemList;
	}

}
