package com.king.desk.gdata.model.bean;

public class ColumnBean {

	private String name;
	private int width;
	public ColumnBean(String name, int width) {
		super();
		this.name = name;
		this.width = width;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
}
