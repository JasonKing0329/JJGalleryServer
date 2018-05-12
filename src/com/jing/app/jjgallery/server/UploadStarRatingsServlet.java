package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.db.DatabaseManager;
import com.jing.app.jjgallery.http.bean.request.UploadStarRatingRequest;
import com.jing.app.jjgallery.http.bean.response.BaseResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet to handle folder surf action
 */
public class UploadStarRatingsServlet extends BaseJsonServlet<UploadStarRatingRequest, BaseResponse> {

    @Override
    protected Class<UploadStarRatingRequest> getRequestClass() {
        return UploadStarRatingRequest.class;
    }

    @Override
    protected void onReceiveRequest(UploadStarRatingRequest requestBean, HttpServletResponse resp) throws IOException {

        BaseResponse response = new BaseResponse();

        try {
            new DatabaseManager().updateStarRatings(requestBean.getRatingList());
            response.setResult(1);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(0);
            response.setMessage(e.getMessage());
        }

        sendResponse(resp, response);
    }
}
