package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.http.bean.request.PathRequest;
import com.jing.app.jjgallery.http.bean.response.OpenFileResponse;
import com.jing.app.jjgallery.util.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * open file on server
 */
public class OpenFileServlet extends BaseJsonServlet<PathRequest, OpenFileResponse> {

    @Override
    protected Class<PathRequest> getRequestClass() {
        return PathRequest.class;
    }

    @Override
    protected void onReceiveRequest(PathRequest requestBean, HttpServletResponse resp) throws IOException {
        if (requestBean == null) {
            return;
        }
        OpenFileResponse responseBean = new OpenFileResponse();
        if (requestBean == null) {
            responseBean.setErrorMessage("null request");
        }
        else {
            if (requestBean.getPath() == null) {
                responseBean.setErrorMessage("null path");
            }
            else {
                // 打开文件夹
                if (requestBean.getName() == null) {
                    try {
                        FileUtil.openFile(requestBean.getPath());
                        responseBean.setSuccess(true);
                    } catch (Exception e) {
                        responseBean.setErrorMessage("打开文件目录失败：" + e.getMessage());
                    }
                }
                // 打开文件
                else {
                    boolean isExist = false;
                    try {
                        for (String type: Constants.videoTypes) {
                            String diskPath = requestBean.getPath() + "/" + requestBean.getName() + type;
                            isExist = new File(diskPath).exists();
                            if (isExist) {
                                FileUtil.openFile(diskPath);
                                responseBean.setSuccess(true);
                                break;
                            }
                        }
                        if (!isExist) {
                            responseBean.setErrorMessage("没有找到文件");
                        }
                    } catch (Exception e) {
                        responseBean.setErrorMessage("打开文件失败：" + e.getMessage());
                    }
                }
            }
        }

        sendResponse(resp, responseBean);
    }

}
