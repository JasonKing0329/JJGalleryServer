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

}
