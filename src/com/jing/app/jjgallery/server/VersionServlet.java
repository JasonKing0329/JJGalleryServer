package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.conf.SettingProperties;
import com.jing.app.jjgallery.http.bean.request.VersionRequest;
import com.jing.app.jjgallery.http.bean.response.BaseResponse;
import com.jing.app.jjgallery.http.bean.response.VersionResponse;
import com.jing.app.jjgallery.util.PropertiesUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * get all kinds of version code
 */
public class VersionServlet extends BaseJsonServlet<VersionRequest, BaseResponse<VersionResponse>> {

    @Override
    protected Class<VersionRequest> getRequestClass() {
        return VersionRequest.class;
    }

    @Override
    protected void onReceiveRequest(VersionRequest requestBean, HttpServletResponse resp) throws IOException {
        if (requestBean == null) {
            return;
        }
        BaseResponse<VersionResponse> responseBean = new BaseResponse();
        VersionResponse response = new VersionResponse();
        responseBean.setData(response);
        if (Constants.UPLOAD_TYPE_DB.equals(requestBean.getType())) {
            response.setVersionCode(SettingProperties.getUploadDbTime(getServletContext()));
        }
        responseBean.setResult(1);
        sendResponse(resp, responseBean);
    }

}
