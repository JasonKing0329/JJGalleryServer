package com.king.service.gdb.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/9 11:43
 */
public class RecordStar {

    private Long id;

    private long recordId;

    private long starId;

    private int type;

    private int score;

    private int scoreC;
    
    private Star star;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getStarId() {
        return this.starId;
    }

    public void setStarId(long starId) {
        this.starId = starId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreC() {
        return this.scoreC;
    }

    public void setScoreC(int scoreC) {
        this.scoreC = scoreC;
    }

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

}
