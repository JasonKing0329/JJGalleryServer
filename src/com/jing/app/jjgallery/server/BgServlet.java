package com.jing.app.jjgallery.server;

import com.google.gson.Gson;
import com.jing.app.jjgallery.http.bean.response.BgResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BgServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BgResponse bean = new BgResponse();
		bean.setImageList(new ArrayList<>());
		String bgFolder = getServletContext().getRealPath("/") + "res/bg";
		File file = new File(bgFolder);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f:files) {
				bean.getImageList().add("res/bg/" + f.getName());
			}
		}
		resp.getWriter().print(new Gson().toJson(bean));
	}
}
