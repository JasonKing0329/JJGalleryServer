package com.king.desk.gdata;

import java.util.ArrayList;
import java.util.List;

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

	public static final String DIR_ALL = "All";

	public static List<String> getDirectoryList() {
		List<String> list = new ArrayList<>();
		list.add("H:/root/G/Actors");
		list.add("H:/root/G/Scenes");
		list.add("H:/root/G/3");
		list.add("H:/root/G/Long");
		list.add("H:/root/G/multi");
		list.add("I:/TDDownload/scene");
		list.add("D:/king/game/other/star");
		list.add("E:/TDDOWNLOAD/Other");
		list.add("F:/myparadise/latestShow/other/star");
		list.add("I:/TDDownload/three-way");
		list.add("F:/myparadise/latestShow/other/together");
		list.add("I:/TDDownload/multi-way");
		return list;
	}

}
