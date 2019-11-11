package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.http.bean.request.PathRequest;
import com.jing.app.jjgallery.http.bean.response.PathResponse;
import com.jing.app.jjgallery.util.ConvertUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class PathServlet extends BaseJsonServlet<PathRequest, PathResponse> {

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
            for (String type:videoTypes) {
                String diskPath = requestBean.getPath() + "/" + requestBean.getName() + type;
                if (new File(diskPath).exists()) {
                    String path = ConvertUtil.convertUrlPath(diskPath);
                    response.setAvailable(true);
                    response.setPath(path);
                    break;
                }
            }
        }
        return response;
    }
}
