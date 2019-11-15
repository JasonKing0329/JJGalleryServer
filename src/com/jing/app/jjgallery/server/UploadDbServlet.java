package com.jing.app.jjgallery.server;

import com.google.gson.Gson;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.SettingProperties;
import com.jing.app.jjgallery.http.bean.response.BaseResponse;
import com.jing.app.jjgallery.http.bean.response.UploadResponse;
import com.jing.app.jjgallery.util.UploadUtil;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class UploadDbServlet extends HttpServlet {

	private Gson gson;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (gson == null) {
			gson = new Gson();
		}
		BaseResponse<UploadResponse> responseBean = new BaseResponse();

		request.setCharacterEncoding("utf-8");	//设置编码

		// 当前servlet上传的文件数量恒定为1
		//获取文件需要上传到的路径
		String path = Configuration.getUploadDbPath(getServletContext());

		try {
			UploadUtil.uploadFiles(request, path);

			responseBean.setResult(1);
			UploadResponse uploadResponse = new UploadResponse();
			responseBean.setData(uploadResponse);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			SettingProperties.setUploadDbTime(getServletContext(), timeStamp);
			uploadResponse.setTimeStamp(timeStamp);
		} catch (FileUploadException e) {
			e.printStackTrace();
			responseBean.setResult(-1);
			responseBean.setMessage(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			responseBean.setResult(-1);
			responseBean.setMessage(e.getMessage());
		}

		sendResponse(response, responseBean);
//		request.getRequestDispatcher("filedemo.jsp").forward(request, response);
	}

	/**
	 * send response
	 * called by extended class
	 * @param resp
	 * @param responseBean
	 * @throws IOException
	 */
	private void sendResponse(HttpServletResponse resp, Object responseBean) throws IOException {
		String json = gson.toJson(responseBean);
		System.out.println(getClass().getName() + "[sendResponse]" + json);
		// 内存中加载的file文件名是utf-8编码的汉字，response的时候要设置下面这个header信息
		resp.setContentType("text/html;charset=UTF-8"); //目的是为了控制浏览器的行为，即控制浏览器用UTF-8进行解码；
		resp.setCharacterEncoding("UTF-8"); //的目的是用于response.getWriter()输出的字符流的乱码问题，如果是response.getOutputStream()是不需要此种解决方案的；因为这句话的意思是为了将response对象中的数据以UTF-8解码后发向浏览器；
		resp.getWriter().print(json);
	}

}