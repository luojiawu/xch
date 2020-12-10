package com.pgy.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgy.common.AuthCode;


/**
 * 验证码处理类
 * @author Administrator
 *
 */
@Controller
public class AuthCodeAction{
	
	public static final String AUTH_CODE = "AUTH_CODE";
	private static final long serialVersionUID = 1L;

	@RequestMapping("verify")
	protected void getAuthCode(HttpSession session, HttpServletResponse response) throws ServletException, IOException {
		//调用AuthCode对象的静态方法，获取返回的验证码
		String code = AuthCode.getAuthCode();
		//将获取到的验证码给到session对象中
		session.setAttribute(AUTH_CODE, code);
		//获取验证码图片
		BufferedImage img = AuthCode.getCodeImg(code);
		//输出验证码图片到客户端ImageIO图片输入流，用于输入图片
						 //图片格式
		ImageIO.write(img, "JPEG", response.getOutputStream());
	}

}
