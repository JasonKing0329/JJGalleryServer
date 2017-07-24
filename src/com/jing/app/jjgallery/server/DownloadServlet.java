package com.jing.app.jjgallery.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;

public class DownloadServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String type = req.getParameter("type");
		if (type != null) {
			File file = getFilePath(type, req);
			if (file != null && file.exists()) {
				resp.setContentType(this.getServletContext().getMimeType(file.getName()));
				resp.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
				resp.setContentLength((int)file.length());

				try {
					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();
					int len = 0;
					byte[] buff = new byte[1024];
					while ((len = fis.read(buff)) > 0) {
						os.write(buff, 0, len);
					}
					fis.close();
					os.flush();
					os.close();

					// 下载完成后
					downloadFinish(type, file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				resp.getWriter().append("Error: file doesn't exist");
			}
		}
	}

	private void downloadFinish(String type, File file) {
		// star和record下载完成后移动到added目录
//		if (type.equals(Command.TYPE_STAR) || type.equals(Command.TYPE_RECORD)) {
//		    moveFile(type, file.getPath());
//		}
	}

	private File getFilePath(String type, HttpServletRequest req) throws UnsupportedEncodingException {
		File file = null;
		if (type.equals(Command.TYPE_APP)) {
			String path = Configuration.getAppPath(getServletContext());
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_GDB_DATABASE)) {
			String path = Configuration.getGdbDatabasePath(getServletContext());
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_RECORD)) {
			String filename = req.getParameter("name");
			filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
			String path = Configuration.getRecordPath(getServletContext()) + "/" + filename;
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_STAR)) {
			String filename = req.getParameter("name");
			filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
			String path = Configuration.getStarPath(getServletContext()) + "/" + filename;
			file = new File(path);
		}
		return file;
	}

}
