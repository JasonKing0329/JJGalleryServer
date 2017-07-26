package com.jing.app.jjgallery.bean.http;

import java.util.List;

public class FolderResponse {

    /**
     * see HttpConstants.FOLDER_TYPE_XX
     */
    private String type;

    private List<FileBean> fileList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FileBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileBean> fileList) {
        this.fileList = fileList;
    }
}
