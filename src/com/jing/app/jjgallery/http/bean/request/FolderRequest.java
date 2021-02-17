package com.jing.app.jjgallery.http.bean.request;

public class FolderRequest {

    private String folder;

    /**
     * see HttpConstants.FOLDER_TYPE_XX
     */
    private String type;

    private boolean isCountSize;

    /**
     * see HttpConstants.FILE_FILTER_XX
     */
    private int filterType;

    /**
     * 访客模式
     */
    private boolean isGuest;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCountSize() {
        return isCountSize;
    }

    public void setCountSize(boolean countSize) {
        isCountSize = countSize;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }
}
