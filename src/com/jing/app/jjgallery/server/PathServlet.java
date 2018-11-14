package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.http.bean.request.PathRequest;
import com.jing.app.jjgallery.http.bean.response.PathResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class PathServlet extends BaseJsonServlet<PathRequest, PathResponse> {

    private String[][] serverMap = new String[][] {
            new String[]{"D:/king/game/other/scene", "videos/scene"},
            new String[]{"E:/TDDOWNLOAD/Other", "videos/e"},
            new String[]{"F:/myparadise/latestShow/other/star", "videos/f_star"},
            new String[]{"F:/myparadise/latestShow/other/three-way", "videos/f_3"},
            new String[]{"F:/myparadise/latestShow/other/multi-way", "videos/f_multi"},
            new String[]{"D:/king/game/other/star", "videos/d_star"}
    };

    protected String[] videoTypes = new String[] {
            ".mp4", ".wmv", ".mkv", ".avi", ".mov", ".flv", "rmvb"
    };

    @Override
    protected Class<PathRequest> getRequestClass() {
        return PathRequest.class;
    }

    @Override
    protected void onReceiveRequest(PathRequest requestBean, HttpServletResponse resp) throws IOException {

        PathResponse response = getResponse(requestBean);
        sendResponse(resp, response);
    }

    private PathResponse getResponse(PathRequest requestBean) {
        PathResponse response = new PathResponse();
        if (requestBean != null && requestBean.getPath() != null) {
            for (int i = 0; i < serverMap.length; i ++) {
                if (requestBean.getPath().startsWith(serverMap[i][0])) {
                    for (String type:videoTypes) {
                        String diskPath = requestBean.getPath() + "/" + requestBean.getName() + type;
                        if (new File(diskPath).exists()) {
                            String path = requestBean.getPath().replace(serverMap[i][0], serverMap[i][1])
                                    + "/" + requestBean.getName() + type;
                            response.setAvailable(true);
                            response.setPath(path);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return response;
    }
}
