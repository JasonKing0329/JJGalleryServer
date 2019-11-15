package com.jing.app.jjgallery.util;

import com.jing.app.jjgallery.conf.Configuration;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

    private static String getPropertiesPath(ServletContext servletContext) {
        return servletContext.getRealPath("/") + Configuration.CONF_NAME;
    }

    /**
     * 获取Properties对象
     * @return
     */
    private static Properties getProperties(ServletContext servletContext) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getPropertiesPath(servletContext)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    /**
     * 根据key查询value值
     * @param key key
     * @return
     */
    public static String getValue(ServletContext context, String key){
        Properties properties = getProperties(context);
        String value = properties.getProperty(key);
        return value;
    }

    /**
     * 新增/修改数据
     * @param key
     * @param value
     */
    public static void setValue(ServletContext context, String key, String value){
        Properties properties = getProperties(context);
        properties.setProperty(key, value);
        String path = getPropertiesPath(context);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
