package com.pgy.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class RecodeUtil {
	
	public static void creatRrCode(String contents, int width, int height,HttpServletResponse response) {
        Hashtable hints = new Hashtable();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //容错级别最高
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  //设置字符编码
        hints.put(EncodeHintType.MARGIN, 0);                //二维码空白区域,最小为0也有白边,只是很小,最小是6像素左右
        
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODABAR, width, height, hints); // 1、读取文件转换为字节数组
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);
            //转换成png格式的IO流
            ImageIO.write(image, "png", response.getOutputStream());
//            byte[] bytes = out.toByteArray();
//            // 2、将字节数组转为二进制
//            BASE64Encoder encoder = new BASE64Encoder();
//            binary = encoder.encodeBuffer(bytes).trim();
        } catch (WriterException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static final int BLACK = -16777216;
    public static final int WHITE = -1;

    /**
     * 条形码编码
     * 
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void barCode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.CODE_128, codeWidth, height, null);
            OutputStream outputStream = new FileOutputStream(imgPath);
            writeToStream(bitMatrix, contents, "png", outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void writeToStream(BitMatrix bitMatrix, String barcode, String format, OutputStream out) throws IOException {
        BufferedImage image = toBufferedImage(bitMatrix, barcode);
        if (!ImageIO.write(image, format, out)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
    
    private static BufferedImage toBufferedImage(BitMatrix matrix, String barcode) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        int[] rowPixels = new int[width];
        BitArray row = new BitArray(width);
        for(int y = 0; y < height; ++y) {
            row = matrix.getRow(y, row);
            for(int x = 0; x < width; ++x) {
                if (y > height - 26){   //最后25像素不填充，用来写数字
                    rowPixels[x] = WHITE;
                }else {
                    rowPixels[x] = row.get(x) ? BLACK : WHITE;
                }
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }
        Graphics graphics = image.getGraphics();
        Font font = new Font("Helvetica", Font.PLAIN,24);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        int strWidth = graphics.getFontMetrics().stringWidth(barcode);
        graphics.drawString(barcode, width/2 - strWidth / 2, height - 3);
        return image;
    }
    


    /**
     * image流数据处理
     *
     * @author ianly
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

}
