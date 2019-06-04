package com.king.desk.gdata.model;

import java.util.Comparator;

import com.king.desk.gdata.Constants.CommonColumn;
import com.king.desk.gdata.model.bean.TableItem;

public class TableComparator implements Comparator<TableItem> {

	private CommonColumn mSortColumn;
	private boolean mDesc;
	
	public TableComparator(CommonColumn column, boolean desc) {
		mSortColumn = column;
		mDesc = desc;
	}
	
	@Override
	public int compare(TableItem left, TableItem right) {

		if (left.getBean() == null) {
			return 0;
		}
		if (right.getBean() == null) {
			return 0;
		}
		switch (mSortColumn) {
		case DATE:
			return sortLong(left.getBean().getLastModifyTime(), right.getBean().getLastModifyTime());
		case NAME:
			return sortString(left.getBean().getName(), right.getBean().getName());
		case DIR:
			return sortString(left.getBean().getDirectory(), right.getBean().getDirectory());
		case SCENE:
			return sortString(left.getBean().getScene(), right.getBean().getScene());
		case SCORE:
			return sortInt(left.getBean().getScore(), right.getBean().getScore());
		case SCORE_FEEL:
			return sortInt(left.getBean().getScoreFeel(), right.getBean().getScoreFeel());
		case STAR:
			return sortInt(left.getBean().getScoreStar(), right.getBean().getScoreStar());
		case PASSION:
			return sortInt(left.getBean().getScorePassion(), right.getBean().getScorePassion());
		case CUM:
			return sortInt(left.getBean().getScoreCum(), right.getBean().getScoreCum());
		case SPECIAL:
			return sortInt(left.getBean().getScoreSpecial(), right.getBean().getScoreSpecial());
		
		default:
			break;
		}
		return 0;
	}
	
	private int sortString(String left, String right) {
		if (left == null) {
			left = "zzzzzzzzzzzzzzzzzzzzzz";
		}
		if (right == null) {
			right = "zzzzzzzzzzzzzzzzzzzzzz";
		}
		if (mDesc) {
			return right.toLowerCase().compareTo(left.toLowerCase());
		}
		else {
			return left.toLowerCase().compareTo(right.toLowerCase());
		}
	}
	
	private int sortInt(int vl, int vr) {
		if (mDesc) {
			if (vl - vr < 0) {
				return 1;
			}
			else if (vl - vr > 0) {
				return -1;
			}
			
		}
		else {
			if (vl - vr < 0) {
				return -1;
			}
			else if (vl - vr > 0) {
				return 1;
			}
		}
		return 0;
	}

	private int sortLong(long vl, long vr) {
		if (mDesc) {
			if (vl - vr < 0) {
				return 1;
			}
			else if (vl - vr > 0) {
				return -1;
			}
			
		}
		else {
			if (vl - vr < 0) {
				return -1;
			}
			else if (vl - vr > 0) {
				return 1;
			}
		}
		return 0;
	}
}
