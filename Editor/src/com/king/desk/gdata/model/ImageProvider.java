package com.king.desk.gdata.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.king.desk.gdata.Conf;

public class ImageProvider {

	public static String getStarRandomImage(String name) {
		String path = null;
		File folder = new File(Conf.STAR_IMG_DIR + "/" + name);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files.length > 0) {
				int index = Math.abs(new Random().nextInt()) % files.length;
				path = files[index].getPath();
			}
		}
		if (path == null) {
			File singleFile = new File(Conf.STAR_IMG_DIR + "/" + name + ".png");
			if (singleFile.exists()) {
				path = singleFile.getPath();
			}
		}
		return path;
	}

	public static String getRecordRandomImage(String name) {
		String path = null;
		File folder = new File(Conf.RECORD_IMG_DIR + "/" + name);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files.length > 0) {
				int index = Math.abs(new Random().nextInt()) % files.length;
				path = files[index].getPath();
			}
		}
		if (path == null) {
			File singleFile = new File(Conf.RECORD_IMG_DIR + "/" + name + ".png");
			if (singleFile.exists()) {
				path = singleFile.getPath();
			}
		}
		return path;
	}

	public static List<String> getRecordRandomImages(String name, int number) {
		List<String> list = new ArrayList<>();
		File folder = new File(Conf.RECORD_IMG_DIR + "/" + name);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files.length > 0) {
				List<File> fileList = new ArrayList<>();
				for (int i = 0; i < files.length; i++) {
					fileList.add(files[i]);
				}
				Collections.shuffle(fileList);
				for (int i = 0; i < fileList.size() && i < number; i++) {
					list.add(fileList.get(i).getPath());
				}
			}
		}
		if (list.size() < number) {
			File singleFile = new File(Conf.RECORD_IMG_DIR + "/" + name + ".png");
			if (singleFile.exists()) {
				list.add(singleFile.getPath());
			}
		}
		return list;
	}
}
