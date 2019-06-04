package com.king.desk.gdata.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class IndexView {
	
	private OnCharSelectListener onCharSelectListener;
	
	public void setOnCharSelectListener(OnCharSelectListener listener) {
		onCharSelectListener = listener;
	}

	public void create(Container container, int x, int y, int width, int height) {
		char charIndex = 'A';
		int eachHeight = height / 26;
		int lastHeight = height - eachHeight * 25;
		for (int i = 0; i < 26; i++) {
			charIndex = (char) ('A' + i);
			JLabel label = new JLabel(String.valueOf(charIndex));
			if (i == 26) {
				label.setBounds(x, y + eachHeight * i, width, lastHeight);
			}
			else {
				label.setBounds(x, y + eachHeight * i, width, eachHeight);
			}
			label.setBackground(Color.white);
			final char indexChar = charIndex;
			label.addMouseListener(new MouseClickListener() {
				
				@Override
				public void onClickRightMouse(MouseEvent event) {

				}
				
				@Override
				public void onClickLeftMouse(MouseEvent event) {
					onCharSelectListener.onSelectChar(indexChar);
				}
			});
			container.add(label);
		}
	}
	
	public interface OnCharSelectListener {
		void onSelectChar(char index);
	}
}
