package com.jing.app.jjgallery.conf;

public class Constants {

    public static String DB_NAME = "gdata.db";

    public static String[][] getFolderMap() {
        return folderMap;
    }

    public static String[][] folderMap = new String[][] {
            new String[]{"D:/king/game/other/scene", "videos/d_scene"},
            new String[]{"D:/king/game/other/star", "videos/d_star"},
            new String[]{"E:/TDDOWNLOAD/Other", "videos/e_star"},
            new String[]{"F:/myparadise/latestShow/other/star", "videos/f_star"},
            new String[]{"F:/myparadise/latestShow/other/three-way", "videos/f_3"},
            new String[]{"F:/myparadise/latestShow/other/multi-way", "videos/f_multi"},
            new String[]{"F:/myparadise/latestShow/temp", "videos/f_temp"}
    };

    public static String[][] folderMap_comp = new String[][] {
            new String[]{"E:/temp/coolg/server_root/d_scene", "videos/d_scene"},
            new String[]{"E:/temp/coolg/server_root/e_star", "videos/e_star"},
            new String[]{"E:/temp/coolg/server_root/f_star", "videos/f_star"},
            new String[]{"E:/temp/coolg/server_root/f_3", "videos/f_3"},
            new String[]{"E:/temp/coolg/server_root/f_multi", "videos/f_multi"},
            new String[]{"E:/temp/coolg/server_root/d_star", "videos/d_star"},
            new String[]{"E:/temp/coolg/server_root/f_temp", "videos/f_temp"}
    };

    public static String[] videoTypes = new String[] {
            ".mp4", ".wmv", ".mkv", ".avi", ".mov", ".flv", "rmvb"
    };

    public static String PREF_KEY_UPLOAD_TIME = "pref_upload_time";

    public static String UPLOAD_TYPE_DB = "upload_type_db";
}
