package com.jing.app.jjgallery.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.jing.app.jjgallery.http.bean.response.GdbMoveResponse;
import com.jing.app.jjgallery.http.bean.request.GdbRequestMoveBean;
import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.Filters;

public class GdbServlet extends BaseJsonServlet<GdbRequestMoveBean, GdbMoveResponse> {

	private String addedPath;

	@Override
	protected Class<GdbRequestMoveBean> getRequestClass() {
		return GdbRequestMoveBean.class;
	}

	@Override
	protected void onReceiveRequest(GdbRequestMoveBean requestBean, HttpServletResponse resp) throws IOException {
		doRequestMove(requestBean, resp);
	}

	private void doRequestMove(GdbRequestMoveBean requestBean, HttpServletResponse resp) throws IOException {

		moveAllFiles(requestBean.getType());

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

	private void moveAllFiles(String type) {
		String folderPath;
		if (type.equals(Command.TYPE_RECORD)) {
			folderPath = Configuration.getRecordPath(getServletContext());
		}
		else {
			folderPath = Configuration.getStarPath(getServletContext());
		}
		addedPath = folderPath + "/" + Filters.FOLDER_ADDED;

		File[] files = new File(folderPath).listFiles();
		// 第一级有过滤目录，并且如果没有目录的话直接在added里面创建目录
		for (File file:files) {
			if (file.isDirectory() && !Filters.isAvailableFolder(file.getName())) {
				continue;
			}
			if (file.isDirectory()) {
				move(file, "");
			}
			else {
				String name = file.getName().substring(0, file.getName().lastIndexOf("."));
				File folder = new File(addedPath + "/" + name);
				if (!folder.exists()) {
					folder.mkdir();
				}
				moveFile(file, folder.getPath());
			}
		}
	}

	private void move(File file, String parent) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f:files) {
				move(f, parent.concat("/").concat(file.getName()));
			}
		}
		else {
			moveFile(file, addedPath + parent);
		}
	}

	private void moveFile(File src, String targetFolder) {
		if (targetFolder != null) {
			File folder = new File(targetFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
		File target = new File(targetFolder + "/" + src.getName());
		if (target.exists()) {
			String name = src.getName().substring(0, src.getName().lastIndexOf("."));
			String extra = src.getName().substring(src.getName().lastIndexOf(".") + 1);
			target = new File(targetFolder + "/" + name + "_" + System.currentTimeMillis() + "." + extra);
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
