package com.king.desk.gdata.adapter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.king.desk.gdata.view.OnDataChangedListener;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordStar;

public class RecordStarAdapter extends AbstractTableModel {

	private String[] columns = new String[] {
			"Type", "id", "starId", "starName", "score", "scoreC"
	};
	
	public static final int COL_TYPE = 0;

	public static final int COL_ID = 1;

	public static final int COL_STAR_ID = 2;

	public static final int COL_STAR_NAME = 3;

	public static final int COL_SCORE = 4;

	public static final int COL_SCORE_C = 5;

	private List<RecordStar> valueList;
	
	private OnDataChangedListener onDataChangedListener;
	
	public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
		this.onDataChangedListener = onDataChangedListener;
	}

	@Override
	public int getColumnCount() {

		return columns.length;
	}

	@Override
	public String getColumnName(int index) {
		return columns[index];
	}

	@Override
	public int getRowCount() {

		return valueList == null ? 0:valueList.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		String value = "";
		RecordStar star = valueList.get(row);
		switch (col) {
		case COL_TYPE:
			switch (star.getType()) {
			case GDBProperites.VALUE_RELATION_TOP:
				value = "Top";
				break;
			case GDBProperites.VALUE_RELATION_BOTTOM:
				value = "Bottom";
				break;
			default:
				value = "Mix";
				break;
			}
			break;
		case COL_ID:
			value = String.valueOf(star.getId());
			break;
		case COL_STAR_ID:
			value = String.valueOf(star.getStarId());
			break;
		case COL_STAR_NAME:
			value = star.getStar().getName();
			break;
		case COL_SCORE:
			value = String.valueOf(star.getScore());
			break;
		case COL_SCORE_C:
			value = String.valueOf(star.getScoreC());
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		RecordStar star = valueList.get(row);
		// 新添加的才能修改
		if (star.getId() == null) {
			// type,id,starId不能修改
			if (col > COL_STAR_ID) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		String strValue = value.toString();
		if (!strValue.equals(getValueAt(row, col))) {
			onDataChangedListener.onDataChanged(value.toString(), row, col);
		}
	}

	public List<RecordStar> getValueList() {
		return valueList;
	}

	public void setValueList(List<RecordStar> valueList) {
		this.valueList = valueList;
	}

}
