package com.pgy.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成类
 * @author Administrator
 *
 */
public class AuthCode {
	//创建随机数对象
	static Random random = new Random();
	/**
	 * 获取验证码数值
	 * @return
	 */
	public static String getAuthCode(){
		StringBuilder code = new StringBuilder();
		//随机生成类
		
		for(int i=0;i<4;i++){
			code.append(random.nextInt(10));
		}
		return code.toString();
	}
	/**
	 * 获取验证码图片
	 * @param code
	 * @return
	 */
	public static BufferedImage getCodeImg(String code){
	
		BufferedImage img = new BufferedImage(88,28,BufferedImage.TYPE_INT_RGB);
		
		Graphics grap = img.getGraphics();
		
		Color backgroundColor = new Color(3, 169, 244);
		
		Color fontColor = new Color(230, 230, 230);
		
		grap.setColor(backgroundColor);
		
		grap.fillRect(0, 0, 88, 28);
		
		grap.setColor(fontColor);
		
		grap.setFont(new Font("宋体",Font.PLAIN,24));
		for(int i=0;i<code.length();i++){
			char c = code.charAt(i);
			grap.drawString(c+"",i*20,20);
		}
		
		/*for(int i=0;i<10;i++){
			int x = random.nextInt(88);
			int x2 = random.nextInt(88);
			int y = random.nextInt(28);
			int y2 = random.nextInt(28);
			grap.drawLine(x, y, x+x2, y+y2);
		}
		*/
		return img;
	}
	
	
}
