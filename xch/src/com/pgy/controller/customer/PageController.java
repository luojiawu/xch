package com.pgy.controller.customer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgy.common.Util;
import com.pgy.service.MainService;
import com.pgy.service.PageService;
import com.pgy.wxjssdk.WxJsSDK;
import com.xch.dto.Goods;
import com.xch.dto.Order;
import com.xch.dto.Site;

import net.sf.json.JSONObject;

/**
 * 页面控制器
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value="/page")
public class PageController {
	
	private static Logger logger = LogManager.getLogger(PageController.class);
	
	PageService pageService = new PageService();
	
	MainService mainService = new MainService();
	
	@RequestMapping(value="/toIndex")
	public String toIndex() {
		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxfc9433c624dde919&redirect_uri=https://move.ehai.xyz/photobook/page/toGoodsList&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
	
	}
	
	@RequestMapping(value="/toSite")
	public String toSite(int siteId,int checkbox,int checkedSiteId,Model data,String orderId) {
		
		if(siteId !=0) {
			
			Site site = mainService.getSiteBySiteId(siteId);
			data.addAttribute("site", site);
		}
		//从地址列表跳转时，如果是默认地址则传该参数
		if(checkbox != 0) {
			data.addAttribute("checkbox", checkbox);
		}
		data.addAttribute("orderId", orderId);
		data.addAttribute("checkedSiteId", checkedSiteId);
		
		return "site";
	}
	
	@RequestMapping(value="/toSiteList")
	public String toSiteList(HttpSession session,String orderId,int checkedSiteId,Model data) {
		
		
		String openId = Util.getSessionOpenId(session);
		
		
		
		List<Site> siteList = mainService.getSiteList(openId);
		
		data.addAttribute("siteList", siteList);
		data.addAttribute("orderId", orderId);
		data.addAttribute("checkedSiteId", checkedSiteId);
		
		return "siteList";
	}
	
	
	
	
	
	@RequestMapping(value="/toGoodsList")
	public String toGoodsList(HttpSession session,String code,Model data) throws UnsupportedEncodingException {
		
		List<Goods> goodsList = new ArrayList<Goods>();
		
		goodsList = pageService.getGoodsList();		
				
		String openId = "";
		
		logger.info("获取的code数据:"+code);
		
		
		/*JSONObject json = WxJsSDK.getWXOpenId(code);
		String openId = (String)json.get("openid");
		adminService.addAndReplaceUser(openId);
		logger.info("获取新的openId数据:"+openId);
		session.setAttribute(Util.SESSION_OPENID, openId);*/
		
		if(code == null || code.equals("") ||  code.equals(session.getAttribute(Util.SESSION_CODE))) {
			openId = Util.getSessionOpenId(session);
			logger.info("获取session中的的openId数据:"+openId);
			if(openId == null) {
				return "redirect:toIndex";
			}
		}else {
			
			
			JSONObject json = WxJsSDK.getWXOpenId(code);
			openId = (String)json.get("openid");
			String unionId = (String)json.get("unionid");
			mainService.addAndReplaceUser(openId,unionId);
			logger.info("获取的openId数据:"+openId);
			session.setAttribute(Util.SESSION_OPENID, openId);
			session.setAttribute(Util.SESSION_CODE, code);
			
		}
		
		
		
		
		
		data.addAttribute("goodsList", goodsList);
		
		return "goodsList";
	}
	
	@RequestMapping(value="/toGoods")
	public String toGoods(String goodsId,Model date) {
		
		Goods goods = pageService.getGoodsById(goodsId);
		
		date.addAttribute("goods", goods);
		
		return "goods";
	}
	 	
	@RequestMapping(value="/toPhotoPage")
	public String toPhotoPage(HttpSession session,int goodsId,int typeId,int defaultPaper,Model data) {
		
		data.addAttribute("goodsId", goodsId);
		data.addAttribute("typeId", typeId);
		data.addAttribute("defaultPaper", defaultPaper);
		
		return "photoPageNew";
	}
	
	@RequestMapping(value="/toPhotoPage2")
	public String toPhotoPage2() {
	
		return "photoPage";
	}
	
	
	
	@RequestMapping(value="/toOrder")
	public String toOrder(String orderId,Model data) {
		
		HashMap<String, Object> map = mainService.getOrderAndSite(orderId);
		
		data.addAttribute("map", map);
		
		return "order";
	}
	
	@RequestMapping(value="/toOrderList")
	public String toOrderList(HttpSession session,Model data) {
		
		String openId = Util.getSessionOpenId(session);
		
		List<Order> orderList = pageService.getOrderListByOpenId(openId);
		
		data.addAttribute("orderList", orderList);
		
		return "orderList";
		
	}
	
	@RequestMapping(value="/toOrderDetails")
	public String toOrderDetails(String orderId,Model data) {
		
		HashMap<String,Object> map = mainService.getOrderAndSite(orderId);
		
		data.addAttribute("payCloseTime", Util.PAYCLOSETIME);
		data.addAttribute("map", map);
		
		
		return "orderDetails";
		
	}
	
	@RequestMapping(value="/toImageSearch")
	public String toImageSearch() {
		
		return "imageSearch";
	}
	
	@RequestMapping(value="/toImageSearchNew")
	 public String toImageSearchNew() {
				
		 return "imageSearchNew";
	 }
	
}
