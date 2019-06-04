package com.king.desk.gdata;

public class Constants {

	public static final String[] source = new String[] {
			"All", "1v1", "3", "Multi", "Together"
	};
	
	public static enum CommonColumn {
		SEQ, SCENE, DIR, NAME, TOP, BOTTOM, MIX, HD, SCORE, SCORE_FEEL,
		STAR, PASSION, BODY, COCK, ASS, CUM, SPECIAL, BAREBACK, DEPRECATED, SPECIAL_DESC,
		SCORE_TOP, SCORE_BOTTOM, SCORE_MIX, SCORE_C_TOP, SCORE_C_BOTTOM, SCORE_C_MIX
		, DATE, TYPE, FK, OTHER
	};
	
	public static CommonColumn convert(int index) {
		CommonColumn column = null;
		CommonColumn[] values = CommonColumn.values();
		for (int i = 0; i < values.length; i++) {
			if (index == values[i].ordinal()) {
				column = values[i];
				break;
			}
		}
		return column;
	}
}
