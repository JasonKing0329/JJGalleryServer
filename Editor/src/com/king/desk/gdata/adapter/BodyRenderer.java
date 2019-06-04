package com.king.desk.gdata.adapter;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.king.desk.gdata.model.bean.TableItem;

/**
 * 主界面单元格的渲染类
 * @author Administrator
 *
 */
public class BodyRenderer extends DefaultTableCellRenderer  {

	private List<TableItem> dataList;
	
	public void setDataList(List<TableItem> dataList) {
		this.dataList = dataList;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (hasFocus) {
			cell.setBackground(Color.CYAN);
		}
		else {
			TableItem item = dataList.get(row);
			if (item.isDeleted()) {
				cell.setBackground(Color.RED);
			}
			else if (item.isBasicChanged() || item.isStarChanged() || item.isDetailChanged()) {
				cell.setBackground(Color.YELLOW);
			}
			else {
				cell.setBackground(Color.WHITE);
			}
		}
		return cell;
	}

}
