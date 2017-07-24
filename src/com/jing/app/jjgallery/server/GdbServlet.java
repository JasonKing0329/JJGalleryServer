package com.jing.app.jjgallery.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jing.app.jjgallery.bean.http.GdbMoveResponse;
import com.jing.app.jjgallery.bean.http.GdbRequestMoveBean;
import com.jing.app.jjgallery.conf.Command;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.url.Urls;

public class GdbServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getRequestURI();
		System.out.println(req.getRequestURL());
		if (url.endsWith(Urls.REQUEST_MOVE)) {
			doRequestMove(req, resp);
		}
	}

	private void doRequestMove(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		GdbMoveResponse responseBean = new GdbMoveResponse();
		
		String acceptjson = parseJson(req);
		Gson gson = new Gson();
		try {
			GdbRequestMoveBean bean = gson.fromJson(acceptjson, GdbRequestMoveBean.class);

			if (bean.getDownloadList() != null) {
				for (int i = 0; i < bean.getDownloadList().size(); i ++) {
					moveFile(bean.getType(), getFilePath(bean.getType(), bean.getDownloadList().get(i).getName()));
				}
			}
			responseBean.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setSuccess(false);
		}
		
		resp.getWriter().print(gson.toJson(responseBean));
	}

	private String parseJson(HttpServletRequest req) {
		String acceptjson = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) req.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			acceptjson = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("parseJson: " + acceptjson);
		return acceptjson;
	}

	private String getFilePath(String type, String filename) throws UnsupportedEncodingException {
		String path = null;
		if (type.equals(Command.TYPE_RECORD)) {
			filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
			path = Configuration.getRecordPath(getServletContext()) + "/" + filename;
		}
    	else if (type.equals(Command.TYPE_STAR)) {
    	    filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
    	    path = Configuration.getStarPath(getServletContext()) + "/" + filename;
		}
		return path;
	}

	private void moveFile(String type, String path) {
		File src = new File(path);
		String targetFolder = Configuration.getStarPath(getServletContext());
		if (type.equals(Command.TYPE_RECORD)) {
			targetFolder = Configuration.getRecordPath(getServletContext());
		}
		targetFolder = targetFolder + "/added/";
		File target = new File(targetFolder + src.getName());
		if (target.exists()) {
			target = new File(targetFolder + "[repeat][" + System.currentTimeMillis() + "]" + src.getName());
		}
		src.renameTo(target);
	}
}
