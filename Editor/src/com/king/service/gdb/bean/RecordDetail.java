package com.king.service.gdb.bean;

public abstract class RecordDetail {

    private Long id;

    private int sequence;
    private int scoreFkType1;
    private int scoreFkType2;
    private int scoreFkType3;
    private int scoreFkType4;
    private int scoreFkType5;
    private int scoreFkType6;
    private int scoreStory;
    private int scoreScene;
    private int scoreRim;
    private int scoreBjob;
    private int scoreForePlay;
    private int scoreRhythm;
    private int scoreCshow;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getSequence() {
        return this.sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public int getScoreFkType1() {
        return this.scoreFkType1;
    }
    public void setScoreFkType1(int scoreFkType1) {
        this.scoreFkType1 = scoreFkType1;
    }
    public int getScoreFkType2() {
        return this.scoreFkType2;
    }
    public void setScoreFkType2(int scoreFkType2) {
        this.scoreFkType2 = scoreFkType2;
    }
    public int getScoreFkType3() {
        return this.scoreFkType3;
    }
    public void setScoreFkType3(int scoreFkType3) {
        this.scoreFkType3 = scoreFkType3;
    }
    public int getScoreFkType4() {
        return this.scoreFkType4;
    }
    public void setScoreFkType4(int scoreFkType4) {
        this.scoreFkType4 = scoreFkType4;
    }
    public int getScoreFkType5() {
        return this.scoreFkType5;
    }
    public void setScoreFkType5(int scoreFkType5) {
        this.scoreFkType5 = scoreFkType5;
    }
    public int getScoreFkType6() {
        return this.scoreFkType6;
    }
    public void setScoreFkType6(int scoreFkType6) {
        this.scoreFkType6 = scoreFkType6;
    }
    public int getScoreStory() {
        return this.scoreStory;
    }
    public void setScoreStory(int scoreStory) {
        this.scoreStory = scoreStory;
    }
    public int getScoreScene() {
        return this.scoreScene;
    }
    public void setScoreScene(int scoreScene) {
        this.scoreScene = scoreScene;
    }
    public int getScoreRim() {
        return this.scoreRim;
    }
    public void setScoreRim(int scoreRim) {
        this.scoreRim = scoreRim;
    }
    public int getScoreBjob() {
        return this.scoreBjob;
    }
    public void setScoreBjob(int scoreBjob) {
        this.scoreBjob = scoreBjob;
    }
    public int getScoreForePlay() {
        return this.scoreForePlay;
    }
    public void setScoreForePlay(int scoreForePlay) {
        this.scoreForePlay = scoreForePlay;
    }
    public int getScoreRhythm() {
        return this.scoreRhythm;
    }
    public void setScoreRhythm(int scoreRhythm) {
        this.scoreRhythm = scoreRhythm;
    }
    public int getScoreCshow() {
        return this.scoreCshow;
    }
    public void setScoreCshow(int scoreCshow) {
        this.scoreCshow = scoreCshow;
    }
    
	public abstract String getFkFormatString();
	
	public abstract String getOtherFormatString();
}
