package com.king.desk.gdata;

import com.king.desk.gdata.view.ConfirmCancelDialog;
import com.king.desk.gdata.view.ConfirmDialog;
import com.king.desk.gdata.view.OnConfirmCancelListener;
import com.king.desk.gdata.view.OnConfirmListener;

public class BaseActivity {

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
