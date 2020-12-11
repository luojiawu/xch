package com.pgy.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pgy.common.Util;
import com.xch.dto.Admin;

/**
 * 登录检查拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
	/**
	 * 在Controller类调用之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		
		// TODO 自动生成的方法存根
		//通过request获取session
		HttpSession session = request.getSession();
		//获取session中存储的登录用户数据
		Admin admin = (Admin)session.getAttribute(Util.SESSION_ADMIN);
		//判断用户数据是否存在
		if(admin !=null){
			System.out.println("---用户存在----");
			//继续执行Controller
			return true;
		}else{
			System.out.println("---用户不存在----");
			//获取前一个请求的Url，定义一个变量
			String Url = request.getRequestURI();
			//将该变量设置到session里，用于LoginAction中获取 
			request.getSession().setAttribute("Url", Url);
			//request.getRequestURI().contains()是获取地址栏上的URL
			String toURL = "/admin";
			
			//用户没有登录    对服务器发起请求         上下文路径                  发起/admin，因在springmvc中设置，等同于发送/admin/login
			response.sendRedirect(request.getContextPath()+toURL);
			return false;
		}
	}
}
