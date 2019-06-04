package com.king.desk.gdata.adapter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.king.service.gdb.bean.Record;

public class StarRecordsAdapter extends AbstractTableModel {

	public static int TABLE_WIDTH = 520;
	
	private List<Record> list;
	
	public void setList(List<Record> list) {
		this.list = list;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}
	
	public int getColumnWidth(int column) {
		int width = 100;
		switch (column) {
		case 0:
			width = 40;
			break;
		case 1:
			width = 140;
			break;
		case 2:
			width = 300;
			break;
		case 3:
			width = 40;
			break;

		default:
			break;
		}
		return width;
	}

	public String columnName(int column) {
		String name = "";
		switch (column) {
		case 0:
			name = "Index";
			break;
		case 1:
			name = "Path";
			break;
		case 2:
			name = "Name";
			break;
		case 3:
			name = "Score";
			break;

		default:
			break;
		}
		return name;
	}

	@Override
	public String getColumnName(int index) {
		return columnName(index);
	}

	@Override
	public int getRowCount() {
		return list == null ? 0:list.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		String value = "";
		switch (col) {
		case 0:
			value = String.valueOf(row + 1);
			break;
		case 1:
			value = list.get(row).getDirectory();
			break;
		case 2:
			value = list.get(row).getName();
			break;
		case 3:
			value = String.valueOf(list.get(row).getScore());
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
}
