package com.king.desk.gdata.adapter;

import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import com.king.service.gdb.bean.Star;

public class StarAdapter implements ListModel<String> {

	private List<Star> list;
	
	public void setList(List<Star> list) {
		this.list = list;
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		
	}

	@Override
	public String getElementAt(int position) {
		return list.get(position).getName();
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
