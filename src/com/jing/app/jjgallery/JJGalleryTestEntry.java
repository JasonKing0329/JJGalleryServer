package com.jing.app.jjgallery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.conf.Configuration;

public class JJGalleryTestEntry {

	public static void main(String[] args) {

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("conf.properties"));
			String path = properties.getProperty("gdb_path");
			System.out.println(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
