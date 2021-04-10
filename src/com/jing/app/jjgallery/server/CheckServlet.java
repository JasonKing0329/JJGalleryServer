package com.jing.app.jjgallery.server;

import com.google.gson.Gson;
import com.jing.app.jjgallery.conf.*;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.http.bean.request.GdbCheckNewFileBean;
import com.jing.app.jjgallery.http.bean.response.AppCheckBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
			String json = new Gson().toJson(bean);
			System.out.println(json);
			resp.getWriter().print(json);
		}
		else {
			resp.getWriter().print("parameter is null");
		}
	}

	private Object checkAppUpdate(String version) {
		AppCheckBean bean = new AppCheckBean();
		String localVersion = SettingProperties.getAppVersion(getServletContext());
		bean.setAppVersion(localVersion);
		String path = Configuration.getAppFilePath(getServletContext());
		File file = new File(path);
		bean.setAppName(file.getName());
		bean.setAppSize(file.length());

		if (compareVersion(localVersion, version) > 0) {
			bean.setAppUpdate(true);
		}
		return bean;
	}

	private Object checkGdbDatabase(String version) {
		AppCheckBean bean = new AppCheckBean();
		String localVersion = SettingProperties.getDatabaseVersion(getServletContext());
		bean.setGdbDabaseVersion(localVersion);
		String path = Configuration.getDbPath(getServletContext()) + "/" + Constants.DB_NAME;
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
		List<DownloadItem> list = getDownloadItems(folder, type, null);
		if (list.size() > 0) {
			bean.setRecordExisted(true);
		}
		bean.setRecordItems(list);
		return bean;
	}

	private Object checkNewStars(String type) {
		GdbCheckNewFileBean bean = new GdbCheckNewFileBean();
		File folder = new File(Configuration.getStarPath(getServletContext()));
		List<DownloadItem> list = getDownloadItems(folder, type, null);
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
	 * @return
	 */
	private List<DownloadItem> getDownloadItems(File folder, String type, String key) {
		File files[] = folder.listFiles();
		List<DownloadItem> list = new ArrayList<>();
		if (files.length > 0) {
			for (File file:files) {
				//
				if (file.isDirectory()) {
					if (Filters.isAvailableFolder(file.getName())) {
						String subKey;
						if (key == null) {
							subKey = file.getName();
						}
						else {
							subKey = key.concat("/").concat(file.getName());
						}
						list.addAll(getDownloadItems(file, type, subKey));
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

					item.setKey(key);
					item.setName(filename);
					item.setSize(file.length());
					item.setFlag(type);
					list.add(item);
				}
			}
		}
		return list;
	}

	/**
	 * 比较本地版本与目标版本
	 * @param localVersion 本地版本
	 * @param targetVersion 目标版本
	 * @return =0代表相等，>0代表localVersion>targetVersion，<0代表localVersion<targetVersion
	 */
	public static int compareVersion(String localVersion, String targetVersion) {
		try {
			System.out.println("localVersion " + localVersion + ", targetVersion " + targetVersion);
			String[] locals = localVersion.split("\\.");
			String[] targets = targetVersion.split("\\.");
			int indexLocal = 0, indexTarget = 0;
			int result = 0;
			// 以点号为分隔，从左到右依次比较各位版本号
			while (indexLocal < locals.length || indexTarget < targets.length) {
				int vLocal = indexLocal < locals.length ? Integer.parseInt(locals[indexLocal]) : 0;
				int vTarget = indexTarget < targets.length ? Integer.parseInt(targets[indexTarget]) : 0;
				result = vLocal - vTarget;
				// 已经比较出大小，不用再比较
				if (result != 0) {
					break;
				}
				// 目前相等，继续比较
				indexLocal ++;
				indexTarget ++;
			}
			return result > 0 ? 1:(result < 0 ? -1:0);
		} catch (Exception e) {

		}
		return 0;
	}
}
