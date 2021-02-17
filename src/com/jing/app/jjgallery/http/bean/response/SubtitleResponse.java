package com.jing.app.jjgallery.http.bean.response;

import com.jing.app.jjgallery.http.bean.data.FileBean;

import java.util.List;

public class SubtitleResponse {

    private List<FileBean> fileList;

    public List<FileBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileBean> fileList) {
        this.fileList = fileList;
    }
}
