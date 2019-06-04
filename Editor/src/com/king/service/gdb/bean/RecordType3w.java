package com.king.service.gdb.bean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/9 11:49
 */
public class RecordType3w extends RecordDetail {

    private int scoreFkType7;
    private int scoreFkType8;

    public int getScoreFkType7() {
        return this.scoreFkType7;
    }
    public void setScoreFkType7(int scoreFkType7) {
        this.scoreFkType7 = scoreFkType7;
    }
    public int getScoreFkType8() {
        return this.scoreFkType8;
    }
    public void setScoreFkType8(int scoreFkType8) {
        this.scoreFkType8 = scoreFkType8;
    }

	@Override
	public String getFkFormatString() {
		StringBuffer buffer = new StringBuffer();
		if (getScoreFkType1() > 0) {
			buffer.append(" Type1(").append(getScoreFkType1()).append(")");
		}
		if (getScoreFkType2() > 0) {
			buffer.append(" Type2(").append(getScoreFkType2()).append(")");
		}
		if (getScoreFkType3() > 0) {
			buffer.append(" Type3(").append(getScoreFkType3()).append(")");
		}
		if (getScoreFkType4() > 0) {
			buffer.append(" Type4(").append(getScoreFkType4()).append(")");
		}
		if (getScoreFkType5() > 0) {
			buffer.append(" Type5(").append(getScoreFkType5()).append(")");
		}
		if (getScoreFkType6() > 0) {
			buffer.append(" Type6(").append(getScoreFkType6()).append(")");
		}
		if (getScoreFkType7() > 0) {
			buffer.append(" Type7(").append(getScoreFkType7()).append(")");
		}
		if (getScoreFkType8() > 0) {
			buffer.append(" Type8(").append(getScoreFkType8()).append(")");
		}
		return buffer.toString();
	}

	@Override
	public String getOtherFormatString() {
		StringBuffer buffer = new StringBuffer();
		if (getScoreBjob() > 0) {
			buffer.append(" Bjob(").append(getScoreBjob()).append(")");
		}
		if (getScoreCshow() > 0) {
			buffer.append(" CShow(").append(getScoreCshow()).append(")");
		}
		if (getScoreForePlay() > 0) {
			buffer.append(" ForePlay(").append(getScoreForePlay()).append(")");
		}
		if (getScoreRhythm() > 0) {
			buffer.append(" Rhythm(").append(getScoreRhythm()).append(")");
		}
		if (getScoreRim() > 0) {
			buffer.append(" Rhythm(").append(getScoreRim()).append(")");
		}
		if (getScoreScene() > 0) {
			buffer.append(" Scene(").append(getScoreScene()).append(")");
		}
		if (getScoreStory() > 0) {
			buffer.append(" Story(").append(getScoreStory()).append(")");
		}
		return buffer.toString();
	}

}
