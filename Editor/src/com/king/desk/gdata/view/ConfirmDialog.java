package com.king.desk.gdata.view;

import javax.swing.JOptionPane;

import com.king.desk.gdata.model.DebugLog;

public class ConfirmDialog {

	private String message;
	private String title;
	private OnConfirmListener listener;
	
	public ConfirmDialog setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public ConfirmDialog setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public ConfirmDialog setListener(OnConfirmListener listener) {
		this.listener = listener;
		return this;
	}
	
	public void show() {
		// 这个会阻塞，直到点确定才继续执行后面的代码
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
		if (listener != null) {
			listener.onConfirm();
		}
	}
}
