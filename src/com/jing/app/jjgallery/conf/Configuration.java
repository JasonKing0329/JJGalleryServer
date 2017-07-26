package com.jing.app.jjgallery.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class Configuration {
	
	private static String basePath;
	private static final String CONF_NAME = "conf.properties";

	public static String getStarPath(ServletContext servletContext) {
		basePath = getBasePath(servletContext);
		return basePath + "/star";
	}

	public static String getRecordPath(ServletContext servletContext) {
		basePath = getBasePath(servletContext);
		return basePath + "/record";
	}

	private static String getBasePath(ServletContext servletContext) {
		if (basePath == null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
				basePath = properties.getProperty("gdb_path");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return basePath;
	}

	public static String getAppVersion(ServletContext servletContext) {
		String appVersion = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			appVersion = properties.getProperty("app_version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appVersion;
	}

	public static String getGdbDatabaseVersion(ServletContext servletContext) {
		String appVersion = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			appVersion = properties.getProperty("gdb_db_version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appVersion;
	}

	public static String getAppPath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("app_update_path");

			File file = new File(path);
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				path = files[0].getPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getGdbDatabasePath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("gdb_db_update_path");

			File file = new File(path);
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				path = files[0].getPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getUploadPath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("upload_path");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getVideoScenePath(ServletContext servletContext) {
		return getProperty(servletContext, "video_scene_path");
	}

	public static String getVideoStarPathE(ServletContext servletContext) {
		return getProperty(servletContext, "video_star_path_e");
	}

	public static String getVideoStarPathD(ServletContext servletContext) {
		return getProperty(servletContext, "video_star_path_d");
	}

	public static String getVideoStarPathDIndex(ServletContext servletContext) {
		return getProperty(servletContext, "video_star_path_d_index");
	}

	private static String getProperty(ServletContext servletContext, String key) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

}
