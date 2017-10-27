package com.jing.app.jjgallery.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.http.bean.response.GdbMoveResponse;
import com.jing.app.jjgallery.http.bean.request.GdbRequestMoveBean;
import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.Filters;

public class GdbServlet extends BaseJsonServlet<GdbRequestMoveBean, GdbMoveResponse> {

	@Override
	protected Class<GdbRequestMoveBean> getRequestClass() {
		return GdbRequestMoveBean.class;
	}

	@Override
	protected void onReceiveRequest(GdbRequestMoveBean requestBean, HttpServletResponse resp) throws IOException {
		doRequestMove(requestBean, resp);
	}

	private void doRequestMove(GdbRequestMoveBean requestBean, HttpServletResponse resp) throws IOException {

		if (requestBean.getDownloadList() != null) {
			for (int i = 0; i < requestBean.getDownloadList().size(); i ++) {
				DownloadItem item = requestBean.getDownloadList().get(i);
				moveFile(requestBean.getType(), getFilePath(requestBean.getType(), item.getName(), item.getKey()), item.getKey());
			}
		}
		removeEmptyFolders(requestBean.getType());
		GdbMoveResponse responseBean = new GdbMoveResponse();
		responseBean.setSuccess(true);

		String tempPath = Configuration.getTempPath(getServletContext());
		File file = new File(tempPath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File f:files) {
				f.delete();
			}
		}

		sendResponse(resp, responseBean);
	}

	private String getFilePath(String type, String filename, String parent) throws UnsupportedEncodingException {
		String path = null;
		if (type.equals(Command.TYPE_RECORD)) {
			filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
			if (parent == null) {
				path = Configuration.getRecordPath(getServletContext()) + "/" + filename;
			}
			else {
				parent = Configuration.getRecordPath(getServletContext()) + "/" + parent;
				path = parent + "/" + filename;
			}
		}
    	else if (type.equals(Command.TYPE_STAR)) {
    	    filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
			if (parent == null) {
				path = Configuration.getStarPath(getServletContext()) + "/" + filename;
			}
			else {
				parent = Configuration.getStarPath(getServletContext()) + "/" + parent;
				path = parent + "/" + filename;
			}
		}
		return path;
	}

	private void moveFile(String type, String path, String parent) {
		File src = new File(path);
		String targetFolder = Configuration.getStarPath(getServletContext());
		if (type.equals(Command.TYPE_RECORD)) {
			targetFolder = Configuration.getRecordPath(getServletContext());
		}
		targetFolder = targetFolder + "/" + Filters.FOLDER_ADDED + "/";
		if (parent != null) {
			targetFolder = targetFolder + parent + "/";
			File folder = new File(targetFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
		File target = new File(targetFolder + src.getName());
		if (target.exists()) {
			target = new File(targetFolder + "[repeat][" + System.currentTimeMillis() + "]" + src.getName());
		}
		System.out.println("[GdbServlet]moveFile src:" + src.getPath() + ", target:" + target.getPath());
		src.renameTo(target);
	}

    /**
     * remove empty folders after moved all successfully
     */
    private void removeEmptyFolders(String type) {
        String targetFolder = Configuration.getStarPath(getServletContext());
        if (type.equals(Command.TYPE_RECORD)) {
            targetFolder = Configuration.getRecordPath(getServletContext());
        }
        File root = new File(targetFolder);
		deleteDir(root);
    }

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory() && Filters.isAvailableFolder(dir.getName())) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		if (Filters.isAvailableFolder(dir.getName())) {
			dir.delete();
		}
		return true;
	}
}
