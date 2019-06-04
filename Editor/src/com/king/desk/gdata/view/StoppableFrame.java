package com.king.desk.gdata.view;

import java.awt.AWTEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class StoppableFrame extends JFrame {
	
	private OnCloseListener onCloseListener;

	public void setOnCloseListener(OnCloseListener onCloseListener) {
		this.onCloseListener = onCloseListener;
	}
	
	public StoppableFrame() {
		//激活窗口事件  
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}
	
	@Override  
    protected void processWindowEvent(WindowEvent e) {  
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
    		if (onCloseListener != null) {
    			if (onCloseListener.stopClose()) {
        			onCloseListener.warningClose();
                    return; //直接返回，阻止默认动作，阻止窗口关闭  
				}
                else {
    				onCloseListener.onWindowClosing();
        		}
    		}
        }
        super.processWindowEvent(e);
    }  
	
	public interface OnCloseListener {
		void warningClose();
		void onWindowClosing();
		boolean stopClose();
	}
}
