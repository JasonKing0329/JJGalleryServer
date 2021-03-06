package com.jing.app.jjgallery.http.bean.data;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DownloadItem {
    /**
     * 用于服务端识别下载内容的关键信息，可以是url
     * 在star与record下载中，key充当parent目录（没有parent则为null）
     * gdb和app update中，key为null
     */
    private String key;
    /**
     * 下载文件的文件名
     */
    private String name;
    /**
     * 下载文件的文件flag
     */
    private String flag;
    /**
     * 下载文件的总大小
     */
    private long size;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
