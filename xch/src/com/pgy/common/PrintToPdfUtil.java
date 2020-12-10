package com.pgy.common;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;





public class PrintToPdfUtil {
	
	private static String fragile = "D:/javaWeb/custom/common/fragile.jpg"; 
	
	private static String black = "D:/javaWeb/custom/common/black.png"; 
	
	//调整比例
	private static double adjust = 4.1666;
	
	/**
	 * 
	 * @param imageFolderPath
	 *            图片文件夹地址
	 * @param pdfPath
	 *            PDF文件保存地址
	 * 
	 */
	public static void toPdf(String imageFolderPath, String pdfPath) {
		try {
			// 图片文件夹地址
			// String imageFolderPath = "D:/Demo/ceshi/";
			// 图片地址
			String imagePath = null;
			// PDF文件保存地址
			// String pdfPath = "D:/Demo/ceshi/hebing.pdf";
			// 输入流
			FileOutputStream fos = new FileOutputStream(pdfPath);
			// 创建文档 
			Document doc = new Document(new Rectangle((float) Util.divide(3661,adjust), (float) Util.divide(5362,adjust)),0,0,0,0);
			
			// 根据图片大小设置文档大小
			doc.setPageSize(new Rectangle((float) Util.divide(3661,adjust), (float) Util.divide(5362,adjust)));
			
			//doc.open();
			// 写入PDF文档
			PdfWriter.getInstance(doc, fos);
			
			
			
			
			// 读取图片流
			BufferedImage img = null;
			// 实例化图片
			Image image = null;
			
			
			
			// 获取图片文件夹对象
			File file = new File(imageFolderPath);
			File[] files = file.listFiles();
			doc.open();
			
			/**
			 * 易碎图标
			 */
			image = Image.getInstance(fragile);
			
			image.scaleToFit((float)Util.divide(708,adjust),(float)Util.divide(708,adjust));
			
			image.setAbsolutePosition((float)Util.divide(277,adjust),(float)Util.divide(252,adjust));
			doc.add(image);
			image.setAbsolutePosition((float)Util.divide(2512,adjust),(float)Util.divide(59,adjust));
			doc.add(image);
			
			image = Image.getInstance(black);
			
			image.scaleToFit((float)Util.divide(59,adjust), (float)Util.divide(59,adjust));
			image.setAbsolutePosition(0,(float)Util.divide(1407,adjust));
			doc.add(image);
			image.setAbsolutePosition(0,(float)Util.divide(3177,adjust));
			doc.add(image);
			image.setAbsolutePosition(0,(float)Util.divide(4949,adjust));
			doc.add(image);
			image.setAbsolutePosition((float)Util.divide(3602,adjust),(float)Util.divide(1407,adjust));
			doc.add(image);
			image.setAbsolutePosition((float)Util.divide(3602,adjust),(float)Util.divide(3177,adjust));
			doc.add(image);
			image.setAbsolutePosition((float)Util.divide(3602,adjust),(float)Util.divide(4949,adjust));
			doc.add(image);
			
			
			// 循环获取图片文件夹内的图片
			for (File file1 : files) {
				if (file1.getName().endsWith(".png")
						|| file1.getName().endsWith(".jpg")
						|| file1.getName().endsWith(".gif")
						|| file1.getName().endsWith(".jpeg")
						|| file1.getName().endsWith(".tif")) {
					// System.out.println(file1.getName());
					imagePath = imageFolderPath + file1.getName();
					
					
					// 实例化图片
					
					image = Image.getInstance(imagePath);
					img = ImageIO.read(new File(imagePath));
					image.scaleToFit((float)Util.divide(img.getWidth(),adjust), (float)Util.divide(img.getHeight(),adjust));
					
					switch (file1.getName()) {
					case "custom_1.jpg":
						// 添加图片到文档
						// 读取图片流
						
						//image.scaleToFit((float)Util.divide(1039,adjust), (float)Util.divide(1429,adjust));
						image.setAbsolutePosition((float)Util.divide(2452,adjust),(float)Util.divide(794,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(2452,adjust),(float)Util.divide(2296,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(2452,adjust),(float)Util.divide(3808,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(277,adjust),(float)Util.divide(1102,adjust));
						doc.add(image);
						break;
					case "custom_2.jpg":
						// 添加图片到文档
						
						//image.scaleToFit((float)Util.divide(886,adjust), (float)Util.divide(1181,adjust));
						image.setAbsolutePosition((float)Util.divide(1482,adjust),(float)Util.divide(179,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(1482,adjust),(float)Util.divide(1433,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(1482,adjust),(float)Util.divide(2688,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(1482,adjust),(float)Util.divide(3946,adjust));
						doc.add(image);
						break;
					case "custom_3.jpg":
						// 添加图片到文档
						//image.scaleToFit((float)Util.divide(1240,adjust), (float)Util.divide(1240,adjust));
						image.setAbsolutePosition((float)Util.divide(168,adjust),(float)Util.divide(2620,adjust));
						doc.add(image);
						image.setAbsolutePosition((float)Util.divide(168,adjust),(float)Util.divide(3926,adjust));
						doc.add(image);
						break;
						
					default:
						break;
					}
				}
			}
			
			// 关闭文档
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		toPdf("D:/javaWeb/custom/13415086855/868542903608/", "D:/javaWeb/custom/13415086855/868542903608/demo.pdf");
		long time2 = System.currentTimeMillis();
		int time = (int) ((time2 - time1)/1000);
		System.out.println("执行了："+time+"秒！");
	}
	
	
}
