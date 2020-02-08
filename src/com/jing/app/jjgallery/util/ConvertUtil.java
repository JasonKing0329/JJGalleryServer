package com.jing.app.jjgallery.util;

import com.jing.app.jjgallery.model.PathBean;
import com.jing.app.jjgallery.model.parser.PathContextParser;

import java.io.File;
import java.util.List;

public class ConvertUtil {

    public static String convertUrlPath(String diskPath) {
        String path = diskPath;
        if (diskPath != null) {
            String javaPath = diskPath.replaceAll("\\\\", "/");
            File file = new File(javaPath);
            if (file.exists()) {
                List<PathBean> pathList = PathContextParser.getInstance().getPathList();
                if (pathList != null) {
                    for (int i = 0; i < pathList.size(); i ++) {
                        if (javaPath.startsWith(pathList.get(i).getDocBase())) {
                            int start = "/JJGalleryServer/".length();
                            path = javaPath.replace(pathList.get(i).getDocBase(), pathList.get(i).getPath().substring(start));
                            break;
                        }
                    }
                }
            }
        }
        return path;
    }

}
