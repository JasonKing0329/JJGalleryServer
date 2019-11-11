package com.jing.app.jjgallery.util;

import com.jing.app.jjgallery.conf.Constants;

import java.io.File;

public class ConvertUtil {

    public static String convertUrlPath(String diskPath) {
        String path = diskPath;
        if (diskPath != null) {
            String javaPath = diskPath.replaceAll("\\\\", "/");
            File file = new File(javaPath);
            if (file.exists()) {
                for (int i = 0; i < Constants.getFolderMap().length; i ++) {
                    if (javaPath.startsWith(Constants.getFolderMap()[i][0])) {
                        path = javaPath.replace(Constants.getFolderMap()[i][0], Constants.getFolderMap()[i][1]);
                        break;
                    }
                }
            }
        }
        return path;
    }

}
