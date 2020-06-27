package com.king.desk.gdata.model;

import com.king.desk.gdata.Conf;
import com.king.desk.gdata.res.R;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Preference {

	private static Properties properties;

	/**
	 * 用于单个main()调试用
	 */
	public static void createIfNotInit() {
		if (properties == null) {
			create();
		}
	}

	public static void create() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(Conf.PROPERTIES));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void updateCreatorProperties() {
		try {
			properties.store(new FileOutputStream(Conf.PROPERTIES), "Update");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Rectangle getMainActivityFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_main_x", 0);
		rectangle.y = getInt("rec_main_y", 0);
		rectangle.width = getInt("rec_main_width", ScreenUtil.getScreenWidth());
		rectangle.height = getInt("rec_main_height", R.dimen.main_frame_height);
		return rectangle;
	}
	
	public static void setMainActivityFrame(Rectangle bounds) {
		properties.setProperty("rec_main_x", String.valueOf(bounds.x));
		properties.setProperty("rec_main_y", String.valueOf(bounds.y));
		properties.setProperty("rec_main_width", String.valueOf(bounds.width));
		properties.setProperty("rec_main_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	public static Rectangle getDeprecatedSelectorFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_deprecated_x", 0);
		rectangle.y = getInt("rec_deprecated_y", 0);
		rectangle.width = getInt("rec_deprecated_width", R.dimen.deprecated_def_width);
		rectangle.height = getInt("rec_deprecated_height", R.dimen.deprecated_def_height);
		return rectangle;
	}

	public static void setDeprecatedSelectorFrame(Rectangle bounds) {
		properties.setProperty("rec_deprecated_x", String.valueOf(bounds.x));
		properties.setProperty("rec_deprecated_y", String.valueOf(bounds.y));
		properties.setProperty("rec_deprecated_width", String.valueOf(bounds.width));
		properties.setProperty("rec_deprecated_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	public static Rectangle getFolderEditorFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_folder_editor_x", 0);
		rectangle.y = getInt("rec_folder_editor_y", 0);
		rectangle.width = getInt("rec_folder_editor_width", R.dimen.deprecated_def_width);
		rectangle.height = getInt("rec_folder_editor_height", R.dimen.deprecated_def_height);
		return rectangle;
	}

	public static void setFolderEditorFrame(Rectangle bounds) {
		properties.setProperty("rec_folder_editor_x", String.valueOf(bounds.x));
		properties.setProperty("rec_folder_editor_y", String.valueOf(bounds.y));
		properties.setProperty("rec_folder_editor_width", String.valueOf(bounds.width));
		properties.setProperty("rec_folder_editor_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	public static Rectangle getRecordStarFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_record_star_x", 100);
		rectangle.y = getInt("rec_record_star_y", 100);
		rectangle.width = getInt("rec_record_star_width", 608);
		rectangle.height = getInt("rec_record_star_height", 365);
		return rectangle;
	}
	
	public static void setRecordStarFrame(Rectangle bounds) {
		properties.setProperty("rec_record_star_x", String.valueOf(bounds.x));
		properties.setProperty("rec_record_star_y", String.valueOf(bounds.y));
		properties.setProperty("rec_record_star_width", String.valueOf(bounds.width));
		properties.setProperty("rec_record_star_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	public static Rectangle getDetailFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_detail_x", 100);
		rectangle.y = getInt("rec_detail_y", 100);
		rectangle.width = getInt("rec_detail_width", 648);
		rectangle.height = getInt("rec_detail_height", 384);
		return rectangle;
	}
	
	public static void setDetailFrame(Rectangle bounds) {
		properties.setProperty("rec_detail_x", String.valueOf(bounds.x));
		properties.setProperty("rec_detail_y", String.valueOf(bounds.y));
		properties.setProperty("rec_detail_width", String.valueOf(bounds.width));
		properties.setProperty("rec_detail_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	public static Rectangle getServerSettingFrame() {
		Rectangle rectangle = new Rectangle();
		rectangle.x = getInt("rec_server_setting_x", 0);
		rectangle.y = getInt("rec_server_setting_y", 0);
		rectangle.width = getInt("rec_server_setting_width", R.dimen.deprecated_def_width);
		rectangle.height = getInt("rec_server_setting_height", R.dimen.deprecated_def_height);
		return rectangle;
	}

	public static void setServerSettingFrame(Rectangle bounds) {
		properties.setProperty("rec_server_setting_x", String.valueOf(bounds.x));
		properties.setProperty("rec_server_setting_y", String.valueOf(bounds.y));
		properties.setProperty("rec_server_setting_width", String.valueOf(bounds.width));
		properties.setProperty("rec_server_setting_height", String.valueOf(bounds.height));
		updateCreatorProperties();
	}

	private static int getInt(String key) {
		return getInt(key, 0);
	}
	
	private static int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int getServerPort() {
		return getInt("server_port", 8080);
	}

	public static void setServerPort(int value) {
		properties.setProperty("server_port", String.valueOf(value));
		updateCreatorProperties();
	}

	public static String getServerName() {
		return properties.getProperty("server_name");
	}

	public static void setServerName(String value) {
		properties.setProperty("server_name", value);
		updateCreatorProperties();
	}
}
