package com.jing.app.jjgallery.conf;

import com.jing.app.jjgallery.util.PropertiesUtil;

import java.io.File;

import javax.servlet.ServletContext;

public class Configuration {
	
	private static String basePath;
	public static final String CONF_NAME = "conf.properties";

	/**
	 * 下载star图片目录
	 * @param servletContext
	 * @return
	 */
	public static String getStarPath(ServletContext servletContext) {
		basePath = getBasePath(servletContext);
		return basePath + "/star";
	}

	/**
	 * 下载record图片目录
	 * @param servletContext
	 * @return
	 */
	public static String getRecordPath(ServletContext servletContext) {
		basePath = getBasePath(servletContext);
		return basePath + "/record";
	}

	/**
	 * gdb根目录
	 * @param servletContext
	 * @return
	 */
	public static String getBasePath(ServletContext servletContext) {
		return PropertiesUtil.getValue(servletContext, "gdb_path");
	}

	/**
	 * 下载前缓存压缩图片文件的目录
	 * @param servletContext
	 * @return
	 */
	public static String getTempPath(ServletContext servletContext) {
		return PropertiesUtil.getValue(servletContext, "temp_catch_path");
	}

	/**
	 * database所在的目录
	 * @param servletContext
	 * @return
	 */
	public static String getDbPath(ServletContext servletContext) {
		return PropertiesUtil.getValue(servletContext, "gdb_db_update_path");
	}

	/**
	 * 上传db文件的目录
	 * @param servletContext
	 * @return
	 */
	public static String getUploadDbPath(ServletContext servletContext) {
		return PropertiesUtil.getValue(servletContext, "upload_path");
	}

	/**
	 * app下载目录
	 * @param servletContext
	 * @return
	 */
	public static String getAppPath(ServletContext servletContext) {
		return PropertiesUtil.getValue(servletContext, "app_update_path");
	}

	public static String getAppFilePath(ServletContext servletContext) {
		String path = getAppPath(servletContext);
		File file = new File(path);
		File[] files = file.listFiles();
		if (files != null && files.length > 0) {
			path = files[0].getPath();
		}
		return path;
	}
}
