package com.king.service.gdb.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/9 11:38
 */
public class Star {

    private Long id;
    private String name;
    private int records;
    private int betop;
    private int bebottom;
    private float average;
    private int max;
    private int min;
    private float caverage;
    private int cmax;
    private int cmin;
    
    private List<Integer> scoreList = new ArrayList<Integer>();
    private List<Integer> scoreCList = new ArrayList<Integer>();


    private int favor;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecords() {
        return this.records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getBetop() {
        return this.betop;
    }

    public void setBetop(int betop) {
        this.betop = betop;
    }

    public int getBebottom() {
        return this.bebottom;
    }

    public void setBebottom(int bebottom) {
        this.bebottom = bebottom;
    }

    public float getAverage() {
        return this.average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public float getCaverage() {
        return this.caverage;
    }

    public void setCaverage(float caverage) {
        this.caverage = caverage;
    }

    public int getCmax() {
        return this.cmax;
    }

    public void setCmax(int cmax) {
        this.cmax = cmax;
    }

    public int getCmin() {
        return this.cmin;
    }

    public void setCmin(int cmin) {
        this.cmin = cmin;
    }

    public int getFavor() {
        return this.favor;
    }

    public void setFavor(int favor) {
        this.favor = favor;
    }

	public void addScore(int score) {
		scoreList.add(score);
	}

	public void addScoreC(int score) {
		scoreCList.add(score);
	}

	public List<Integer> getScoreList() {
		return scoreList;
	}

	public List<Integer> getScoreCList() {
		return scoreCList;
	}

}
