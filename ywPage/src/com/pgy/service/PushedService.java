package com.pgy.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.pgy.common.Util;
import com.pgy.controller.move.PushedController;
import com.pgy.dao.PEveningPagerDao;
import com.pgy.dao.PPositiveDao;
import com.pgy.dto.P_Ana;
import com.pgy.dto.P_EveningPager;
import com.pgy.dto.P_Template;
import com.pgy.dto.P_User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PushedService {
	
	private static Logger logger = LogManager.getLogger(PushedController.class);
	
	static PPositiveDao positiveDao = null;
	
	PEveningPagerDao eveningPagerDao = null;
	
	/**
	 * 发送信息至客服接口
	 */									
	private static String customerUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send"; 
	/**
	 * 获取token接口
	 */				
	private static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
	/**
	 * 分享页面
	 */
	private static String shareUrl = "https://move.ehai.xyz/photobook/s/toShare";
	
	
	public void pushedCopyAna(String openId,int templateType,String aUrl) {
		
		JSONObject content = setContentCopyAna(openId,templateType,aUrl);
        
        String url = customerUrl+"?access_token="+getToKen();
        logger.info("url-->"+url);
        HttpEntity entity = new StringEntity(content.toString(), "UTF-8");
        byte[] result = Util.doPost(url, entity);
        String resultStr = "";
		try {
			resultStr = new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("调用客服发送接口返回数据-->"+resultStr);
		
	}
	
	public void pushed(String openId,String title,String description,String aUrl,String picurl) {
		
		JSONObject content = setContent(openId, title, description, aUrl, picurl);
		
        String url = customerUrl+"?access_token="+getToKen();
        logger.info("url-->"+url);
        HttpEntity entity = new StringEntity(content.toString(), "UTF-8");
        byte[] result = Util.doPost(url, entity);
        String resultStr = "";
		try {
			resultStr = new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("调用客服发送接口-->openId:"+openId+",发送结果:"+resultStr);
		
	}
	
	
	public void pushedReadLog(String openId,long time) {
		
		
		
		String aUrl = "http://move.ehai.xyz/ywPositive/p/toClient";
		JSONObject content = setContentReadLog(openId,time,aUrl);
        
        String url = customerUrl+"?access_token="+getToKen();
        logger.info("url-->"+url);
        HttpEntity entity = new StringEntity(content.toString(), "UTF-8");
        byte[] result = Util.doPost(url, entity);
        String resultStr = "";
		try {
			resultStr = new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("调用客服发送接口返回数据-->"+resultStr);
		
	}
	
	
	public static JSONObject setContentReadLog(String WxOpenID,long time,String aUrl) {
		
		
		JSONObject text = new JSONObject();
		JSONObject json = new JSONObject();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date(time*1000);
    	String d = dateFormat.format(date);
		text.put("content", "有用户阅读了您分享的文章《忆我正能量 "+d+"》\n\n<a style=\"color:blue;\" href=\""+aUrl+"\">点这查看详情</a>");
		json.put("touser", WxOpenID);
		json.put("msgtype", "text");
		json.put("text", text);
		
		return json;
	}
	
	/**
	 * 设置发送消息的格式数据
	 * @param WxOpenID openId
	 * @param ArtID 忆我文书的文章id
	 * @param picurl
	 * @return
	 */
	public static JSONObject setContentCopyAna(String WxOpenID,int templateType,String aUrl) {
		
		
		JSONObject text = new JSONObject();
		JSONObject json = new JSONObject();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String d = dateFormat.format(date);
    	if(templateType == 1) {
    		text.put("content", "文章已换成您的名片\n\n标题：《忆我正能量 "+d+"》\n\n<a href=\""+aUrl+"\">点击领取</a>\n\n(并订阅同类爆文)");
    	}else if(templateType == 2) {
    		text.put("content", "文章已换成您的晚报\n\n标题：《电商晚报 "+d+"》\n\n<a href=\""+aUrl+"\">点击领取</a>\n\n(并订阅同类爆文)");
    	}
		
		json.put("touser", WxOpenID);
		json.put("msgtype", "text");
		json.put("text", text);
		
		return json;
	}
	
	public static void main(String[] args) {
		
		
		
		String[] recommend = "0,5,18".split(",");
		
		//获取分类中的随机数 长度要减一，不然会下标越界
		String CID = recommend[getrandom(0, recommend.length-1)];
		
		String url = Util.articleUrl+CID;
		
		JSONArray dataArray = JSONArray.fromObject(Util.doGet(url).get("data"));
		
		JSONObject data;
		
		int dataInt = 0;
		
		for(int f=0;f<dataArray.size();f++) {
			
			data = JSONObject.fromObject(dataArray.get(f));
			
			if(CID.equals(data.getString("CID"))) {
				dataInt = f;
				break;
			} 
			
		}
		
		JSONObject json = JSONObject.fromObject(dataArray.get(dataInt));
		
		JSONArray ArtList = JSONArray.fromObject(json.get("ArtList"));
		
		JSONObject art = JSONObject.fromObject(ArtList.get(getrandom(0, ArtList.size())));
		
		
		
		System.out.println(art.toString());
		
	}
	
	/**
	 * 设置发送消息的格式数据
	 * @param WxOpenID openId
	 * @param ArtID 忆我文书的文章id
	 * @param picurl
	 * @return
	 */
	public static JSONObject setContent(String WxOpenID,String title,String description,String aUrl,String picurl) {
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject articles = new JSONObject();
		JSONObject content = new JSONObject();
		
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String d = dateFormat.format(date);
    	if(templateType == 1) {
    		json.put("title", "《忆我正能量 "+d+"》");
    		
    	}else if(templateType == 2) {
    		json.put("title", "《电商晚报 "+d+"》");
    		
    	}*/
		json.put("title", title);
    	json.put("description", description);
    	json.put("url", aUrl);
    	json.put("picurl", picurl);
    	array.add(json);
    	articles.put("articles", array);
		content.put("touser", WxOpenID);
		content.put("msgtype", "news");
		content.put("news", articles);
		
		return content;
	}
	
	
	/**
	 * 获取token
	 * @return
	 */
	public String getToKen() {
		//忆我文书小程序的appid和secret
		String appid = Util.APPID ;
		
		String secret = Util.APPSECRET;
		
		String url = tokenUrl+"?grant_type=client_credential&appid="+appid+"&secret="+secret;
		
		JSONObject jsonObject = Util.doGet(url);
		
		return (String) jsonObject.get("access_token");
		
	}
	
	/**
	 * 推送文章和打卡
	 * @param timeType 时间类型:0为17点45分的晚报,1为9点的打卡,2为10点的文章,3为15点的文章,4为20点30分的文章,
	 * @return
	 */
	public String pushedArticle(int timeType) {
		P_User user = new P_User();
		P_Ana ana = new P_Ana();
		
		positiveDao = new PPositiveDao();
		eveningPagerDao = new PEveningPagerDao();
		List<P_User> userList = new ArrayList<P_User>();
		String aUrl;
		
		SimpleDateFormat dateFormat;
		Date date;
		String d;
		String title;
		P_Template template;
		P_EveningPager eveningPager;
		String[] recommend;
		String CID;
		String url;
		String artUrl;
		JSONArray dataArray;
		JSONObject data;
		int dataInt = 0;
		JSONArray ArtList;
		JSONObject art;
		String text;
		
		switch (timeType) {
		case 0:
			
			
			user.setEveningPager(1);
			userList = positiveDao.getUserByPushedType(user);
			
			/*dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	date = new Date();
	    	d = dateFormat.format(date);
	    	title = "《电商晚报 "+d+"》";*/
	    	title = "《电商晚报》";
	    	
			eveningPager = eveningPagerDao.getEveningPaperByPagerId(eveningPagerDao.getEveningpaperId());
			
			ana.setTemplateId(eveningPager.getPagerId());
			aUrl = "http://move.ehai.xyz/ywPositive/e/toCreateEveningPager";
			for(int i=0;i<userList.size();i++) {
				ana.setOpenId(userList.get(i).getOpenId());
				
				//防止重复生成的方法，如果后面需要更改问好之类的参数，再来加个更新anaTime的
				P_Ana resultAna = positiveDao.getAnaIdByAna(ana);
				//防止重复推送，只有数据库该文章为空时，才推送
				if(resultAna == null){
					pushed(userList.get(i).getOpenId(), title, eveningPager.getPagerTitle(), aUrl, "https://move.ehai.xyz/ywPositive/img/ywIcon.jpg");
				}
			}
			
			break;
		case 1:
			
			
			user.setClockIn(1);
			userList = positiveDao.getUserByPushedType(user);
			
			/*dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	date = new Date();
	    	d = dateFormat.format(date);
	    	title = "《忆我正能量 "+d+"》";*/
	    	title = "《忆我正能量》";
			template = positiveDao.getTemplateByTemplateId(handleTemplateId());
			ana.setTemplateId(template.getTemplateId());
			aUrl = "http://move.ehai.xyz/ywPositive/p/toCreateAna";
			for(int i=0;i<userList.size();i++) {
				
				ana.setOpenId(userList.get(i).getOpenId());
				
				//防止重复生成的方法，如果后面需要更改问好之类的参数，再来加个更新anaTime的
				P_Ana resultAna = positiveDao.getAnaIdByAna(ana);
				//防止重复推送，只有数据库该文章为空时，才推送
				if(resultAna == null){
					pushed(userList.get(i).getOpenId(), title, template.getContent(), aUrl, template.getPicUrl());
				}
				
				
			}
			
			break;
		case 2:
			
			user.setRecommendTime_m(1);
			
			userList = positiveDao.getUserByPushedType(user);
			
			for(int i=0;i<userList.size();i++) {
				
				recommend = userList.get(i).getRecommend().split(",");
				
				//获取分类中的随机数 长度要减一，不然会下标越界
				CID = recommend[getrandom(0, recommend.length-1)];
				logger.info("用户-->"+userList.get(i).getNickname()+",recommend:"+recommend+",随机获取CID:"+CID);
				url = Util.articleUrl+CID;
				
				dataArray = JSONArray.fromObject(Util.doGet(url).get("data"));
				
				//循环得出CID等于用户随机分类的下标
				for(int f=0;f<dataArray.size();f++) {
					
					data = JSONObject.fromObject(dataArray.get(f));
					
					if(CID.equals(data.getString("CID"))) {
						dataInt = f;
						break;
					} 
					
				}
				//用下标获取对应的json,以及获取里面的ArtList数组
				ArtList = JSONArray.fromObject(JSONObject.fromObject(dataArray.get(dataInt)).get("ArtList"));
				
				logger.info("随机文章数据-->ArtList"+ArtList.toString());
				
				//获取ArtList中的随机文章并转换成json 长度要减一，不然会下标越界
				art = JSONObject.fromObject(ArtList.get(getrandom(0, ArtList.size()-1)));
				
				logger.info("随机文章数据-->"+art.toString());
				
				artUrl = Util.articleSUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				text = JSONObject.fromObject(JSONArray.fromObject(JSONObject.fromObject(JSONArray.fromObject(Util.doGet(artUrl).get("data")).get(0)).getString("Content")).get(0)).getString("text");
				
				aUrl = shareUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				logger.info("推送数据-->openId"+userList.get(i).getOpenId()+",aUrl:"+aUrl);
				
				pushed(userList.get(i).getOpenId(), art.getString("Title"), text, aUrl, art.getString("PhotoCover"));
				
			}
			break;
		case 3:
			user.setRecommendTime_n(1);
			userList = positiveDao.getUserByPushedType(user);
			for(int i=0;i<userList.size();i++) {
				
				recommend = userList.get(i).getRecommend().split(",");
				
				//获取分类中的随机数 长度要减一，不然会下标越界
				CID = recommend[getrandom(0, recommend.length-1)];
				logger.info("用户-->"+userList.get(i).getNickname()+",recommend:"+recommend+",随机获取CID:"+CID);
				url = Util.articleUrl+CID;
				
				dataArray = JSONArray.fromObject(Util.doGet(url).get("data"));
				
				//循环得出CID等于用户随机分类的下标
				for(int f=0;f<dataArray.size();f++) {
					
					data = JSONObject.fromObject(dataArray.get(f));
					
					if(CID.equals(data.getString("CID"))) {
						dataInt = f;
						break;
					} 
					
				}
				//用下标获取对应的json,以及获取里面的ArtList数组
				ArtList = JSONArray.fromObject(JSONObject.fromObject(dataArray.get(dataInt)).get("ArtList"));
				
				logger.info("随机文章数据-->ArtList"+ArtList.toString());
				
				//获取ArtList中的随机文章并转换成json 长度要减一，不然会下标越界
				art = JSONObject.fromObject(ArtList.get(getrandom(0, ArtList.size()-1)));
				
				logger.info("随机文章数据-->"+art.toString());
				
				artUrl = Util.articleSUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				text = JSONObject.fromObject(JSONArray.fromObject(JSONObject.fromObject(JSONArray.fromObject(Util.doGet(artUrl).get("data")).get(0)).getString("Content")).get(0)).getString("text");
				
				aUrl = shareUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				logger.info("推送数据-->openId"+userList.get(i).getOpenId()+",aUrl:"+aUrl);
				
				pushed(userList.get(i).getOpenId(), art.getString("Title"), text, aUrl, art.getString("PhotoCover"));
				
			}
			break;
		case 4:
			user.setRecommendTime_e(1);
			userList = positiveDao.getUserByPushedType(user);
			for(int i=0;i<userList.size();i++) {
				
				recommend = userList.get(i).getRecommend().split(",");
				
				//获取分类中的随机数 长度要减一，不然会下标越界
				CID = recommend[getrandom(0, recommend.length-1)];
				logger.info("用户-->"+userList.get(i).getNickname()+",recommend:"+recommend+",随机获取CID:"+CID);
				url = Util.articleUrl+CID;
				
				dataArray = JSONArray.fromObject(Util.doGet(url).get("data"));
				
				//循环得出CID等于用户随机分类的下标
				for(int f=0;f<dataArray.size();f++) {
					
					data = JSONObject.fromObject(dataArray.get(f));
					
					if(CID.equals(data.getString("CID"))) {
						dataInt = f;
						break;
					} 
					
				}
				//用下标获取对应的json,以及获取里面的ArtList数组
				ArtList = JSONArray.fromObject(JSONObject.fromObject(dataArray.get(dataInt)).get("ArtList"));
				
				logger.info("随机文章数据-->ArtList"+ArtList.toString());
				
				//获取ArtList中的随机文章并转换成json 长度要减一，不然会下标越界
				art = JSONObject.fromObject(ArtList.get(getrandom(0, ArtList.size()-1)));
				
				logger.info("随机文章数据-->"+art.toString());
				
				artUrl = Util.articleSUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				text = JSONObject.fromObject(JSONArray.fromObject(JSONObject.fromObject(JSONArray.fromObject(Util.doGet(artUrl).get("data")).get(0)).getString("Content")).get(0)).getString("text");
				
				aUrl = shareUrl+"?WxOpenID=odpn74vbYUBZNuc-v_J0P6VYJiPg"+"&ArtID="+art.getString("ArtID");
				logger.info("推送数据-->openId"+userList.get(i).getOpenId()+",aUrl:"+aUrl);
				
				pushed(userList.get(i).getOpenId(), art.getString("Title"), text, aUrl, art.getString("PhotoCover"));
				
			}
			break;

		default:
			break;
		}
		
		return "";
		
	}
	
	/**
	 * 获取随机数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getrandom(int start,int end) {
		
		int num=(int) (Math.random()*(end-start+1)+start);
		return num;
	}
	
	/**
	 * 根据当天日期，获取对应模板
	 * @return
	 */
	public static int handleTemplateId() {
		
		positiveDao = new PPositiveDao();
		
		//获取当天时间戳
		long l = System.currentTimeMillis()/1000;
		//计算当天与系统初始日期相隔的天数
		Long s = (getTimesmorning()+(24*60*60)-Util.templateStartTime)/(24*60*60);
		//获取语录总数，因为分了白天和晚上，所以除以2。
	 	int templateNum = positiveDao.getTemplateCount()/2;
		//用相隔日期取余总数，余数则为anaId
		int templateId = (int) (s%templateNum);
		SimpleDateFormat tm = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		
		try {
			//如果不在6-18点这个时间内的，就按晚上给模板id
			if(!belongCalendar(tm.parse(tm.format(date)), tm.parse("06:00:00"), tm.parse("18:00:00"))) {
				templateId+= templateNum;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return templateId;
	}
	
	/**
     * 获取当天0点0分0秒（00:00:00）
     *
     * @return
     */
    private static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        //设置当天时间，后面的三个参数是时分秒
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date beginOfDate = cal.getTime();
        
        return beginOfDate.getTime()/1000;
    }
	
	/**
	 * 判断时间是否在时间段内
	 * 
	 * @param time
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendar(Date time, Date beginTime,
			Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(time);
 
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);
 
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
 
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
	
}
