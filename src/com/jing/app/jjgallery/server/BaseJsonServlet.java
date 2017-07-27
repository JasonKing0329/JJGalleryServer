package com.jing.app.jjgallery.server;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * base servlet to handle json request and json response
 * about get request method, the json content is included in 'data' parameter
 * @param <T> request bean
 * @param <M> response bean
 */
public abstract class BaseJsonServlet<T, M> extends HttpServlet {

    private Gson gson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getParameter("data");
        System.out.println(getClass().getName() + "[doGet]" + json);
        T requestBean = convertJsonRequest(json);
        onReceiveRequest(requestBean, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = parseJson(req);
        System.out.println(getClass().getName() + "[doPost]" + json);
        T requestBean = convertJsonRequest(json);
        onReceiveRequest(requestBean, resp);
    }

    private T convertJsonRequest(String json) {
        if (gson == null) {
            gson = new Gson();
        }
        T requestBean = null;
        try {
            requestBean = gson.fromJson(json, getRequestClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBean;
    }

    protected abstract Class<T> getRequestClass();

    protected abstract void onReceiveRequest(T requestBean, HttpServletResponse resp) throws IOException;

    /**
     * send response
     * called by extended class
     * @param resp
     * @param responseBean
     * @throws IOException
     */
    protected void sendResponse(HttpServletResponse resp, M responseBean) throws IOException {
        String json = gson.toJson(responseBean);
        System.out.println(getClass().getName() + "[sendResponse]" + json);
        // 内存中加载的file文件名是utf-8编码的汉字，response的时候要设置下面这个header信息
        resp.setContentType("text/html;charset=UTF-8"); //目的是为了控制浏览器的行为，即控制浏览器用UTF-8进行解码；
        resp.setCharacterEncoding("UTF-8"); //的目的是用于response.getWriter()输出的字符流的乱码问题，如果是response.getOutputStream()是不需要此种解决方案的；因为这句话的意思是为了将response对象中的数据以UTF-8解码后发向浏览器；
        resp.getWriter().print(json);
    }

    private String parseJson(HttpServletRequest req) {
        String acceptjson = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
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
        System.out.println("parseJson: " + acceptjson);
        return acceptjson;
    }

}
