package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.db.DatabaseManager;
import com.jing.app.jjgallery.http.bean.request.GetStarRatingsRequest;
import com.jing.app.jjgallery.http.bean.response.BaseResponse;
import com.jing.app.jjgallery.http.bean.response.GetStarRatingResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet to handle folder surf action
 */
public class GetStarRatingsServlet extends BaseJsonServlet<GetStarRatingsRequest, BaseResponse<GetStarRatingResponse>> {

    @Override
    protected Class<GetStarRatingsRequest> getRequestClass() {
        return GetStarRatingsRequest.class;
    }

    @Override
    protected void onReceiveRequest(GetStarRatingsRequest requestBean, HttpServletResponse resp) throws IOException {

        BaseResponse<GetStarRatingResponse> response = new BaseResponse<>();
        GetStarRatingResponse responseBean = new GetStarRatingResponse();
        responseBean.setStarId(requestBean.getStarId());
        response.setData(responseBean);

        try {
            responseBean.setRatingList(new DatabaseManager().queryStarRatings(requestBean.getStarId()));
            response.setResult(1);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(0);
            response.setMessage(e.getMessage());
        }

        sendResponse(resp, response);
    }
}
