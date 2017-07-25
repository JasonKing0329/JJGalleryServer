package com.jing.app.jjgallery.conf;

public class Filters {

    public static String FOLDER_ADDED = "added";
    public static String FOLDER_OTHER = "Other";

    public static boolean isAvailableFolder(String name) {
        if (FOLDER_ADDED.equals(name)) {
            return false;
        }
        else if (FOLDER_OTHER.equals(name)) {
            return false;
        }
        return true;
    }

}
