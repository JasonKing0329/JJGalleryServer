package com.king.desk.gdata.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 只把mouseClicked事件暴露出来
 * @author Administrator
 *
 */
public abstract class MouseClickListener implements MouseListener {

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2) {
			onDoubleClick(event);
		}
		else {
			if (event.getButton() == MouseEvent.BUTTON3) {
				onClickRightMouse(event);
			}
			else if (event.getButton() == MouseEvent.BUTTON1) {
				onClickLeftMouse(event);
			}
		}
	}

	protected void onDoubleClick(MouseEvent event) {
		
	}

	public abstract void onClickLeftMouse(MouseEvent event);

	public abstract void onClickRightMouse(MouseEvent event);

}
