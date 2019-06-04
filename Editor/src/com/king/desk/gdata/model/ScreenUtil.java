package com.king.desk.gdata.model;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenUtil {

	public static int getScreenWidth() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		return (int) screensize.getWidth();
	}
	
	public static int getScreenHeight() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		return (int) screensize.getHeight();
	}
}
