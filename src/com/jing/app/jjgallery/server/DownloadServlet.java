package com.jing.app.jjgallery.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.conf.SettingProperties;
import com.jing.app.jjgallery.util.ImgCompress;
import com.jing.app.jjgallery.util.MD5Util;

public class DownloadServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String type = req.getParameter("type");
		if (type != null) {
			File file = getFilePath(type, req);
			if (file != null && file.exists()) {

				System.out.println("doGet " + file.getPath());

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
				System.out.println("doGet " + "Error: file doesn't exist");

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

	private File getFilePath(String type, HttpServletRequest req) throws IOException {
		File file = null;
		if (type.equals(Command.TYPE_APP)) {
			String path = Configuration.getAppFilePath(getServletContext());
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_GDB_DATABASE)) {
			String path = Configuration.getDbPath(getServletContext()) + "/" + Constants.DB_NAME;
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_GDB_DATABASE_UPLOAD)) {
			String path = Configuration.getUploadDbPath(getServletContext()) + "/" + Constants.DB_NAME;
			file = new File(path);
		}
		else if (type.equals(Command.TYPE_RECORD)) {
			String key = req.getParameter("key");
			String filename = req.getParameter("name");
			String root = Configuration.getRecordPath(getServletContext());
			return getImagePath(key, filename, root);
		}
		else if (type.equals(Command.TYPE_STAR)) {
			String key = req.getParameter("key");
			String filename = req.getParameter("name");
			String root = Configuration.getStarPath(getServletContext());
			return getImagePath(key, filename, root);
		}
		System.out.println("[DownloadServlet]" + file.getPath());
		return file;
	}

	private File getImagePath(String key, String filename, String root) throws IOException {
		filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");

		// 先从压缩缓存目录里获取
		String md5 = MD5Util.get16MD5Capital(key + "/" + filename);
		String tempPath = Configuration.getTempPath(getServletContext());
		String targetPath = tempPath + "/" + md5 + ".png";
		File resultFile = new File(targetPath);
		if (resultFile.exists()) {
			return resultFile;
		}

		// 没有压缩缓存过按照新文件处理
		String path = root + "/" + filename;
		File file = new File(path);
		if (!file.exists()) {
			path = root + "/" + key + "/" + filename;
			file = new File(path);
		}
		return compressImageFile(file, targetPath);
	}

	/**
	 * 压缩图片到targetPath
	 * @param file
	 * @param targetPath
	 * @throws IOException
	 */
	private File compressImageFile(File file, String targetPath) throws IOException {
		File compressedFile = file;
		// gif压缩无效，不压缩
		if (file != null && file.exists() && !file.getName().endsWith(".gif")) {
			// 压缩图片
			new ImgCompress()
					.source(file.getPath())
					.setTargetPath(targetPath)
					.resizeFix(1280, 1280)
					.asFile();
			compressedFile = new File(targetPath);
		}
		return compressedFile;
	}

}
