package com.jing.app.jjgallery.conf;

import com.jing.app.jjgallery.util.PropertiesUtil;

import javax.servlet.ServletContext;

public class SettingProperties {

    /**
     * 服务端app更新的版本号
     * @param servletContext
     * @return
     */
    public static String getAppVersion(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, "app_version");
    }

    /**
     * 当前服务端database的版本号
     * @param servletContext
     * @return
     */
    public static String getDatabaseVersion(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, "gdb_db_version");
    }

    /**
     * 上传db文件的时间点记录
     * @param servletContext
     * @return
     */
    public static String getUploadDbTime(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, Constants.PREF_KEY_UPLOAD_TIME);
    }

    /**
     * 设置上传db文件的时间点
     * @param servletContext
     * @param value
     */
    public static void setUploadDbTime(ServletContext servletContext, String value) {
        PropertiesUtil.setValue(servletContext, Constants.PREF_KEY_UPLOAD_TIME, value);
    }

    /**
     * server.xml位置
     * @param servletContext
     * @return
     */
    public static String getServerXmlPath(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, "server_xml_path");
    }

    /**
     * server名称
     * @param servletContext
     * @return
     */
    public static String getServerName(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, "server_name");
    }

    /**
     * udp广播间隔
     * @param servletContext
     * @return
     */
    public static int getUdpTime(ServletContext servletContext) {
        String text = PropertiesUtil.getValue(servletContext, "udp_time");
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 10000;// 默认10秒
        }
    }

    /**
     * conf_tv.xml位置
     * @param servletContext
     * @return
     */
    public static String getConfTvXmlPath(ServletContext servletContext) {
        return PropertiesUtil.getValue(servletContext, "conf_tv_xml_path");
    }

}
