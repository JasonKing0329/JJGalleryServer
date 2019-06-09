package com.king.desk.gdata.adapter;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

public class TextAdapter implements ListModel<String> {

	private List<String> list;
	
	public void setList(List<String> list) {
		this.list = list;
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		
	}

	@Override
	public String getElementAt(int position) {
		return list.get(position);
	}

	@Override
	public int getSize() {
		return list == null ? 0:list.size();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
