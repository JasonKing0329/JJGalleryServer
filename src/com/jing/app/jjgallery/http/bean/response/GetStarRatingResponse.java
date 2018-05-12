package com.jing.app.jjgallery.http.bean.response;

import com.jing.app.jjgallery.http.bean.data.StarRating;

import java.util.List;

public class GetStarRatingResponse {

    private long starId;

    private List<StarRating> ratingList;

    public long getStarId() {
        return starId;
    }

    public void setStarId(long starId) {
        this.starId = starId;
    }

    public List<StarRating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<StarRating> ratingList) {
        this.ratingList = ratingList;
    }
}
