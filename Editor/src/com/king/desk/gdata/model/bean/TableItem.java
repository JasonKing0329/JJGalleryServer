package com.king.desk.gdata.model.bean;

import java.util.List;

import com.king.desk.gdata.Constants;
import com.king.desk.gdata.Constants.CommonColumn;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.RecordDetail;
import com.king.service.gdb.bean.RecordStar;

public class TableItem {

    private int indexInTotalList;

    private boolean isBasicChanged;

    private boolean isStarChanged;

    private boolean isDetailChanged;

    private boolean isDeleted;

    private Record bean;

    private List<RecordStar> stars;

    private String top;

    private String bottom;

    private String mix;

    private String scoreTop;

    private String scoreBottom;

    private String scoreMix;

    private String scoreCTop;

    private String scoreCBottom;

    private String scoreCMix;

    private String date;

    private RecordDetail detail;

    private boolean isAutoCountScore = true;

    public boolean isAutoCountScore() {
        return isAutoCountScore;
    }

    public void setAutoCountScore(boolean isAutoCountScore) {
        this.isAutoCountScore = isAutoCountScore;
    }

    public Record getBean() {
        return bean;
    }

    public void setBean(Record bean) {
        this.bean = bean;
    }

    public String getValueAt(int position) {
        String value = "";
        if (position < CommonColumn.values().length) {
            switch (Constants.convert(position)) {
                case SEQ:// 序号由列表所在row决定
                    break;
                case SCENE:
                    value = bean.getScene();
                    break;
                case DIR:
                    value = bean.getDirectory();
                    break;
                case TOP:
                    value = top;
                    break;
                case BOTTOM:
                    value = bottom;
                    break;
                case MIX:
                    value = mix;
                    break;
                case NAME:
                    value = bean.getName();
                    break;
                case HD:
                    value = String.valueOf(bean.getHdLevel());
                    break;
                case SCORE:
                    value = String.valueOf(bean.getScore());
                    break;
                case SCORE_FEEL:
                    value = String.valueOf(bean.getScoreFeel());
                    break;
                case STAR:
                    value = String.valueOf(bean.getScoreStar());
                    break;
                case PASSION:
                    value = String.valueOf(bean.getScorePassion());
                    break;
                case BODY:
                    value = String.valueOf(bean.getScoreBody());
                    break;
                case COCK:
                    value = String.valueOf(bean.getScoreCock());
                    break;
                case ASS:
                    value = String.valueOf(bean.getScoreAss());
                    break;
                case CUM:
                    value = String.valueOf(bean.getScoreCum());
                    break;
                case SPECIAL:
                    value = String.valueOf(bean.getScoreSpecial());
                    break;
                case BAREBACK:
                    value = String.valueOf(bean.getScoreBareback());
                    break;
                case DEPRECATED:
                    value = String.valueOf(bean.getDeprecated());
                    break;
                case SPECIAL_DESC:
                    value = String.valueOf(bean.getSpecialDesc());
                    break;
                case SCORE_TOP:
                    value = scoreTop;
                    break;
                case SCORE_BOTTOM:
                    value = scoreBottom;
                    break;
                case SCORE_MIX:
                    value = scoreMix;
                    break;
                case SCORE_C_TOP:
                    value = scoreCTop;
                    break;
                case SCORE_C_BOTTOM:
                    value = scoreCBottom;
                    break;
                case SCORE_C_MIX:
                    value = scoreCMix;
                    break;
                case DATE:
                    value = date;
                    break;
                case TYPE:
                    value = getType();
                    break;
                case FK:
                    value = getFk();
                    break;
                case OTHER:
                    value = getOther();
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    public int getIndexInTotalList() {
        return indexInTotalList;
    }

    public void setIndexInTotalList(int indexInTotalList) {
        this.indexInTotalList = indexInTotalList;
    }

    public boolean isBasicChanged() {
        return isBasicChanged;
    }

    public void setBasicChanged(boolean isChanged) {
        this.isBasicChanged = isChanged;
    }

    public boolean isStarChanged() {
        return isStarChanged;
    }

    public void setStarChanged(boolean isStarChanged) {
        this.isStarChanged = isStarChanged;
    }

    public boolean isDetailChanged() {
        return isDetailChanged;
    }

    public void setDetailChanged(boolean isDetailChanged) {
        this.isDetailChanged = isDetailChanged;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean delete) {
        this.isDeleted = delete;
    }

    public List<RecordStar> getStars() {
        return stars;
    }

    public void setStars(List<RecordStar> stars) {
        this.stars = stars;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getMix() {
        return mix;
    }

    public void setMix(String mix) {
        this.mix = mix;
    }

    public String getScoreTop() {
        return scoreTop;
    }

    public void setScoreTop(String scoreTop) {
        this.scoreTop = scoreTop;
    }

    public String getScoreBottom() {
        return scoreBottom;
    }

    public void setScoreBottom(String scoreBottom) {
        this.scoreBottom = scoreBottom;
    }

    public String getScoreMix() {
        return scoreMix;
    }

    public String getScoreCTop() {
        return scoreCTop;
    }

    public void setScoreCTop(String scoreCTop) {
        this.scoreCTop = scoreCTop;
    }

    public String getScoreCBottom() {
        return scoreCBottom;
    }

    public void setScoreCBottom(String scoreCBottom) {
        this.scoreCBottom = scoreCBottom;
    }

    public String getScoreCMix() {
        return scoreCMix;
    }

    public void setScoreCMix(String scoreCMix) {
        this.scoreCMix = scoreCMix;
    }

    public void setScoreMix(String scoreMix) {
        this.scoreMix = scoreMix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        String type = "";
        switch (bean.getType()) {
            case GDBProperites.VALUE_RECORD_TYPE_1V1:
                type = "1v1";
                break;
            case GDBProperites.VALUE_RECORD_TYPE_3W:
                type = "3W";
                break;
            case GDBProperites.VALUE_RECORD_TYPE_MULTI:
                type = "Multi";
                break;
            case GDBProperites.VALUE_RECORD_TYPE_TOGETHER:
                type = "Together";
                break;
            default:
                break;
        }
        return type;
    }

    public void setDetail(RecordDetail detail) {
        this.detail = detail;
    }

    public RecordDetail getDetail() {
        return detail;
    }

    public String getFk() {
        if (detail != null) {
            return detail.getFkFormatString();
        }
        return null;
    }

    public String getOther() {
        if (detail != null) {
            return detail.getOtherFormatString();
        }
        return null;
    }

}
