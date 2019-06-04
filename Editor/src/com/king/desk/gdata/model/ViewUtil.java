package com.king.desk.gdata.model;

import java.awt.FontMetrics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ViewUtil {

	public static void setIcon(JButton iconButton, String file, int width, int height) {
		ImageIcon icon = new ImageIcon(file);
		Image temp = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		icon = new ImageIcon(temp);
		iconButton.setIcon(icon);
	}
	
	public static void setLabelText(JLabel jLabel, String text) {
		if (text == null) {
			return;
		}
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = text.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < text.length()) {
            while (true) {
                len++;
                if (start + len > text.length())break;
                if (fontMetrics.charsWidth(chars, start, len) 
                        > jLabel.getBounds().getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len-1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, text.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }
	
}
