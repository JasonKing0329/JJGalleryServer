package com.king.service.gdb.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/9 11:46
 */
public class Record {

    private Long id;

    private String scene;
    private String directory;
    private String name;
    private int hdLevel;
    private int score;
    private int scoreFeel;
    private int scoreStar;
    private int scorePassion;
    private int scoreCum;
    private int scoreSpecial;
    private int scoreBareback;
    private int deprecated;
    private String specialDesc;
    private long lastModifyTime;
    private int type;
    private long recordDetailId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScene() {
        return this.scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHdLevel() {
        return this.hdLevel;
    }

    public void setHdLevel(int hdLevel) {
        this.hdLevel = hdLevel;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreFeel() {
        return this.scoreFeel;
    }

    public void setScoreFeel(int scoreFeel) {
        this.scoreFeel = scoreFeel;
    }

    public int getScoreStar() {
        return this.scoreStar;
    }

    public void setScoreStar(int scoreStar) {
        this.scoreStar = scoreStar;
    }

    public int getScorePassion() {
        return this.scorePassion;
    }

    public void setScorePassion(int scorePassion) {
        this.scorePassion = scorePassion;
    }

    public int getScoreCum() {
        return this.scoreCum;
    }

    public void setScoreCum(int scoreCum) {
        this.scoreCum = scoreCum;
    }

    public int getScoreSpecial() {
        return this.scoreSpecial;
    }

    public void setScoreSpecial(int scoreSpecial) {
        this.scoreSpecial = scoreSpecial;
    }

    public int getScoreBareback() {
        return this.scoreBareback;
    }

    public void setScoreBareback(int scoreBareback) {
        this.scoreBareback = scoreBareback;
    }

    public int getDeprecated() {
        return this.deprecated;
    }

    public void setDeprecated(int deprecated) {
        this.deprecated = deprecated;
    }

    public String getSpecialDesc() {
        return this.specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    public long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

	public long getRecordDetailId() {
		return recordDetailId;
	}

	public void setRecordDetailId(long recordDetailId) {
		this.recordDetailId = recordDetailId;
	}

}
