package com.king.desk.gdata;

import com.king.desk.gdata.view.ConfirmCancelDialog;
import com.king.desk.gdata.view.ConfirmDialog;
import com.king.desk.gdata.view.OnConfirmCancelListener;
import com.king.desk.gdata.view.OnConfirmListener;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {

	public void showDialog() {
		Rectangle rectangle = customInitSize();
		if (rectangle != null) {
			setPreferredSize(new Dimension(rectangle.width, rectangle.height));
			setLocation(rectangle.x, rectangle.y);
		}
		onShow();
		pack();
		setVisible(true);
	}

	protected abstract void onShow();

	protected abstract Rectangle customInitSize();

	public void showConfirm(String message) {
		showConfirm(message, null, null);
	}

	public void showConfirm(String message, OnConfirmListener listener) {
		showConfirm(message, null, listener);
	}
	
	public void showConfirm(String message, String title, OnConfirmListener listener) {
		new ConfirmDialog()
			.setTitle(title)
			.setMessage(message)
			.setListener(listener)
			.show();
	}

	public void showConfirmCancel(String message, OnConfirmCancelListener listener) {
		showConfirmCancel(message, null, listener);
	}

	public void showConfirmCancel(String message, String title, OnConfirmCancelListener listener) {
		new ConfirmCancelDialog()
			.setTitle(title)
			.setMessage(message)
			.setListener(listener)
			.show();
	}
	
}
