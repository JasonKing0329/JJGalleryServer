package com.king.desk.gdata.model.bean;

import java.util.List;

public class SaveData {

	private String version;
	
	private boolean deleteStarWithoutRecords;
	
	private List<TableItem> list;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isDeleteStarWithoutRecords() {
		return deleteStarWithoutRecords;
	}

	public void setDeleteStarWithoutRecords(boolean deleteStarWithoutRecords) {
		this.deleteStarWithoutRecords = deleteStarWithoutRecords;
	}

	public List<TableItem> getList() {
		return list;
	}

	public void setList(List<TableItem> list) {
		this.list = list;
	}
	
}
