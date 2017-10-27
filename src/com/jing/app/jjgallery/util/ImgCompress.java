package com.jing.app.jjgallery.util;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;

/**
 * 图片压缩处理
 * @author 崔素强
 */
public class ImgCompress {
    private Image img;
    private int width;
    private int height;
    private String sourcePath;
    private String targetPath;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        System.out.println("开始：" + System.currentTimeMillis());
        new ImgCompress()
                .source("E:/temp/31954-100.jpg")
                .setTargetPath("E:/temp/31954-1001.jpg")
                .resizeFix(1920, 1920)
                .asFile();
        System.out.println("结束：" + System.currentTimeMillis());
    }
    public void asFile() throws IOException {
        resize(width, height);
    }
    /**
     * 构造函数
     */
    public ImgCompress() {
    }

    public ImgCompress source(String path) throws IOException {
        sourcePath = path;
        File file = new File(sourcePath);// 读入文件
        img = ImageIO.read(file);      // 构造Image对象
        width = img.getWidth(null);    // 得到源图宽
        height = img.getHeight(null);  // 得到源图长
        return this;
    }

    public ImgCompress setTargetPath(String path) {
        this.targetPath = path;
        return this;
    }

    /**
     * 按照宽度还是高度进行压缩
     * @param w int 最大宽度
     * @param h int 最大高度
     */
    public ImgCompress resizeFix(int w, int h) throws IOException {
        if (width / height > w / h) {
            resizeByWidth(w);
        } else {
            resizeByHeight(h);
        }
        return this;
    }
    /**
     * 以宽度为基准，等比例放缩图片
     * @param w int 新宽度
     */
    public ImgCompress resizeByWidth(int w) throws IOException {
        int h = (int) (height * w / width);
        if (w < width) {
            width = w;
        }
        if (h < height) {
            height = h;
        }
        return this;
    }
    /**
     * 以高度为基准，等比例缩放图片
     * @param h int 新高度
     */
    public ImgCompress resizeByHeight(int h) throws IOException {
        int w = (int) (width * h / height);
        if (w < width) {
            width = w;
        }
        if (h < height) {
            height = h;
        }
        return this;
    }
    /**
     * 强制压缩/放大图片到固定的大小
     * @param w int 新宽度
     * @param h int 新高度
     */
    public ImgCompress resize(int w, int h) throws IOException {
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        File destFile = new File(targetPath);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image); // JPEG编码
        out.close();
        return this;
    }
}