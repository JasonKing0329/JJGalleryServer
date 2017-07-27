package com.jing.app.jjgallery.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jing.app.jjgallery.http.bean.response.AppCheckBean;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.http.bean.request.GdbCheckNewFileBean;
import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.Filters;

public class CheckServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		if (type != null) {
			Object bean = null;
			if (type.equals(Command.TYPE_APP)) {
				bean = checkAppUpdate(req.getParameter("version"));
			}
			else if (type.equals(Command.TYPE_GDB_DATABASE)) {
				bean = checkGdbDatabase(req.getParameter("version"));
			}
			else if (type.equals(Command.TYPE_STAR)) {
				bean = checkNewStars(type);
			}
			else if (type.equals(Command.TYPE_RECORD)) {
				bean = checkNewRecords(type);
			}
			resp.getWriter().print(new Gson().toJson(bean));
		}
		else {
			resp.getWriter().print("parameter is null");
		}
	}

	private Object checkAppUpdate(String version) {
		AppCheckBean bean = new AppCheckBean();
		String localVersion = Configuration.getAppVersion(getServletContext());
		bean.setAppVersion(localVersion);
		String path = Configuration.getAppPath(getServletContext());
		File file = new File(path);
		bean.setAppName(file.getName());
		bean.setAppSize(file.length());
		
		if (localVersion.compareTo(version) > 0) {
			bean.setAppUpdate(true);
		}
		return bean;
	}

	private Object checkGdbDatabase(String version) {
		AppCheckBean bean = new AppCheckBean();
		String localVersion = Configuration.getGdbDatabaseVersion(getServletContext());
		bean.setGdbDabaseVersion(localVersion);
		String path = Configuration.getGdbDatabasePath(getServletContext());
		File file = new File(path);
		bean.setGdbDabaseName(file.getName());
		bean.setGdbDabaseSize(file.length());
		
		if (localVersion.compareTo(version) > 0) {
			bean.setGdbDatabaseUpdate(true);
		}
		return bean;
	}

	private Object checkNewRecords(String type) {
		GdbCheckNewFileBean bean = new GdbCheckNewFileBean();
		File folder = new File(Configuration.getRecordPath(getServletContext()));
		List<DownloadItem> list = getDownloadItems(folder, type, false);
		if (list.size() > 0) {
			bean.setRecordExisted(true);
		}
		bean.setRecordItems(list);
		return bean;
	}

	private Object checkNewStars(String type) {
		GdbCheckNewFileBean bean = new GdbCheckNewFileBean();
		File folder = new File(Configuration.getStarPath(getServletContext()));
		List<DownloadItem> list = getDownloadItems(folder, type, false);
		if (list.size() > 0) {
			bean.setStarExisted(true);
		}
		bean.setStarItems(list);
		return bean;
	}

	/**
	 * 在star与record下载中，item的key充当parent目录
	 * @param folder
	 * @param type
	 * @param isSub
	 * @return
	 */
	private List<DownloadItem> getDownloadItems(File folder, String type, boolean isSub) {
		File files[] = folder.listFiles();
		List<DownloadItem> list = new ArrayList<>();
		if (files.length > 0) {
			for (File file:files) {
				//
				if (file.isDirectory()) {
					if (Filters.isAvailableFolder(file.getName())) {
						list.addAll(getDownloadItems(file, type, true));
					}
					else {
						continue;
					}
				}
				else {
					DownloadItem item = new DownloadItem();
					String filename = file.getName();
					try {
						filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// 二级文件，key是其父目录
					if (isSub) {
						item.setKey(folder.getName());
					}
					// 一级文件，key是去掉后缀的文件名
					else {
						item.setKey(null);
					}
					item.setName(filename);
					item.setSize(file.length());
					item.setFlag(type);
					list.add(item);
				}
			}
		}
		return list;
	}

}
