package com.king.desk.gdata.view;

import javax.swing.JOptionPane;

public class ConfirmCancelDialog {

	private String message;
	private String title;
	private OnConfirmCancelListener listener;
	
	public ConfirmCancelDialog setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public ConfirmCancelDialog setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public ConfirmCancelDialog setListener(OnConfirmCancelListener listener) {
		this.listener = listener;
		return this;
	}
	
	public void show() {
		int result = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        // 1才是否
        if (result == 0) {
        	listener.onConfirm();
		}
        else {
			listener.onCancel();
		}
	}
}
