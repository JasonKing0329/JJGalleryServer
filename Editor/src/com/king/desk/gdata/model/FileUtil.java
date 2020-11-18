package com.king.desk.gdata.model;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static void openFile(String path) throws IOException{
		Desktop.getDesktop().open(new File(path));
	}
	
	public static void moveFile(File file, String targetPath) {
		File targetFile = new File(targetPath + "/" + file.getName());
		if (targetFile.exists()) {
			targetFile.delete();
		}
		file.renameTo(targetFile);
	}
	
	public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        byte[] buffer=new byte[2097152];
        int readByte = 0;
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }

        in.close();
        out.close();
	}

    public static void deleteFile(File removeFile) {
	    if (removeFile.isDirectory()) {
            File[] files = removeFile.listFiles();
            for (File f:files) {
                deleteFile(f);
            }
        }
        removeFile.delete();
    }
}