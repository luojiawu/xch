package com.pgy.controller.move;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgy.common.Util;

import net.sf.json.JSONObject;

/**
 * 微信推送消息控制器
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value="/pushed")
public class PushedController {
	
	private static Logger logger = LogManager.getLogger(PushedController.class);
	
	/**
	 * 消息推送EncodingAESKey密钥  需要对消息进行加密时使用，暂不使用
	 */
	//private static String EncodingAESKey = "90WP31DsTlJqRdEiWufg3H8zCULOyyb0Kb0zYuay8f5";
	/**
	 * 发送信息至客服接口
	 */									
	private static String customerUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send"; 
	/**
	 * 获取token接口
	 */				
	private static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
	
	/**
	 * 消息推送token
	 */
	private static String token = "pgyAppletNotify";
	
	/**
	 * 获取消息推送接口，并在里面调用客服发送消息接口发送消息给用户
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/appletNotify")
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("客服结果通知");
		
		String WxOpenID = "";
		
		String[] ArtID = null;
		
		String picurl = "";
		
		try {
		
			InputStream inStream = request.getInputStream();
			/**
			 * 获取get请求的参数，生成sign与微信发来的比较，正确则继续执行后面的代码
			 */
			String get = request.getQueryString();
	        logger.info("小程序客服的数据："+get);
	        String[] gets = get.split("&");
			
			String signature = gets[0].split("signature=")[1];
			String timestamp = gets[1].split("timestamp=")[1];
			String nonce = gets[2].split("nonce=")[1];
			String echostr = gets[3].split("nonce=")[1];
			response.getWriter().write(echostr);
			List<String> list=new ArrayList<String>();
			list.add(token);
			list.add(timestamp);
			list.add(nonce);
			Collections.sort(list);
			String sign = "";
			for(int i=0;i<list.size();i++) {
				sign += list.get(i);
			}
			sign = shaEncode(sign);
	        if(sign.equals(signature)) {
	        	logger.info("signature验证正确："+sign);
	        	
	        	ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		        byte[] buffer = new byte[1024];
		        int len = 0;
		        while ((len = inStream.read(buffer)) != -1) {
		            outSteam.write(buffer, 0, len);
		        }
		        outSteam.close();
		        inStream.close();
		        String resultStr  = new String(outSteam.toByteArray(),"utf-8");
		        
		        JSONObject json = JSONObject.fromObject(resultStr);
		        logger.info("客服发送消息json："+json.toString());
		        WxOpenID = (String) json.get("FromUserName");
		        logger.info("WxOpenID-FromUserName："+WxOpenID);
		        
		        String PagePath = (String) json.get("PagePath");
		        ArtID = PagePath.split("=");
		        
		        logger.info("ArtID-artId："+ArtID[1]);
		        
		        picurl = (String) json.get("ThumbUrl");
		        
		        logger.info("picurl-ThumbUrl："+picurl);
		        
		        JSONObject content = setContent(WxOpenID, ArtID[1], picurl);
		        
		        String url = customerUrl+"?access_token="+getToKen();
		        HttpEntity entity = new StringEntity(content.toString(), "UTF-8");
		        byte[] result = Util.doPost(url, entity);
		        resultStr = new String(result, "UTF-8");
		        logger.info("调用客服发送接口返回数据-->"+resultStr);
	        	
	        }
	        //通知微信.异步确认成功.必写.不然微信会一直通知后台.八次之后就认为交易失败了.
	        response.getWriter().write("success");
	        
	        
			
		} catch (Exception e) {
			
	        try {
	        	response.getWriter().write("success");
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
		}
		
	}
	
	@RequestMapping(value="/ss")
	public static void ss() throws UnsupportedEncodingException {
		
		JSONObject content = setContent("oCQl61n8HPXB9PyZ6d8F1FIBGxz0", "11", "11");
        
        String url = customerUrl+"?access_token="+getToKen();
        logger.info("url-->"+url);
        HttpEntity entity = new StringEntity(content.toString(), "UTF-8");
        byte[] result = Util.doPost(url, entity);
        String resultStr = new String(result, "UTF-8");
        logger.info("调用客服发送接口返回数据-->"+resultStr);
		
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		
	}
	
	/**
     * @Comment SHA1实现
     * @Author Ron
     * @Date 2017年9月13日 下午3:30:36
     * @return
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
 
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
	
    /**
	 * 设置发送消息的格式数据
	 * @param WxOpenID openId
	 * @param ArtID 忆我文书的文章id
	 * @param picurl
	 * @return
	 */
	public static JSONObject setContent(String WxOpenID,String ArtID,String picurl) {
		
		
		JSONObject text = new JSONObject();
		JSONObject json = new JSONObject();
		
		text.put("content", "推送文章<a href=\"https://move.ehai.xyz/photobook/s/toShare?WxOpenID=odpn74lKA2-piKlzSAZ5TKUsrAVw&ArtID=2841\"> 城里的房子不能卖！卖了就赶紧赎回来！</a>");
		json.put("touser", WxOpenID);
		json.put("msgtype", "text");
		json.put("text", text);
		
		return json;
	}
    
	/**
	 * 获取token
	 * @return
	 */
	public static String getToKen() {
		//忆我文书小程序的appid和secret
		String appid = Util.APPID_2 ;
		
		String secret = Util.APPSECRET_2;
		
		String url = tokenUrl+"?grant_type=client_credential&appid="+appid+"&secret="+secret;
		
		JSONObject jsonObject = Util.doGet(url);
		
		return (String) jsonObject.get("access_token");
		
	}
	
}
