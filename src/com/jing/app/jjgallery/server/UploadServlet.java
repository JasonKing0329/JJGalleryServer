package com.jing.app.jjgallery.server;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.conf.SettingProperties;
import com.jing.app.jjgallery.http.bean.response.BaseResponse;
import com.jing.app.jjgallery.http.bean.response.UploadResponse;
import com.jing.app.jjgallery.util.PropertiesUtil;
import com.jing.app.jjgallery.util.UploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jing.app.jjgallery.conf.Configuration;

/**
 *
 * @author Administrator
 * 文件上传
 * 具体步骤：
 * 1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包
 * 2） 利用 request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同
 * 3）对 DiskFileItemFactory 对象设置一些 属性
 * 4）高水平的API文件上传处理  ServletFileUpload upload = new ServletFileUpload(factory);
 * 目的是调用 parseRequest（request）方法  获得 FileItem 集合list ，
 *
 * 5）在 FileItem 对象中 获取信息，   遍历， 判断 表单提交过来的信息 是否是 普通文本信息  另做处理
 * 6）
 *    第一种. 用第三方 提供的  item.write( new File(path,filename) );  直接写到磁盘上
 *    第二种. 手动处理
 *
 */
public class UploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");	//设置编码

		//获取文件需要上传到的路径
		String path = Configuration.getUploadDbPath(getServletContext());
		try {
			UploadUtil.uploadFiles(request, path);
			responseMsg(response, "success");
		} catch (FileUploadException e) {
			e.printStackTrace();
			responseMsg(response, "FileUploadException: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseMsg(response, "Exception: " + e.getMessage());
		}
	}

	private void responseMsg(HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(message);
	}
}