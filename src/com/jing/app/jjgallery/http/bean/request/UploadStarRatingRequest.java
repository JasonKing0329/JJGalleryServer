package com.jing.app.jjgallery.http.bean.request;

import com.jing.app.jjgallery.http.bean.data.StarRating;

import java.util.List;

public class UploadStarRatingRequest {

    private List<StarRating> ratingList;

    public List<StarRating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<StarRating> ratingList) {
        this.ratingList = ratingList;
    }
}
