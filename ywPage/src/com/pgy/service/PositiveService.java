package com.pgy.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.pgy.common.Util;
import com.pgy.dao.PEveningPagerDao;
import com.pgy.dao.PPositiveDao;
import com.pgy.dto.P_Ana;
import com.pgy.dto.P_EveningPager;
import com.pgy.dto.P_ReadLog;
import com.pgy.dto.P_Template;
import com.pgy.dto.P_User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PositiveService {
	
	private static Logger logger = LogManager.getLogger(PositiveService.class);
	
	
	
	private static String positivePageUrl = "http://move.ehai.xyz/ywPositive/p/toPositive";
	
	private static String eveningPaperPageUrl = "http://move.ehai.xyz/ywPositive/e/toEveningPager";
	
	PPositiveDao positiveDao = null;
	
	PEveningPagerDao eveningPagerDao = null;
	
	public static void main(String[] args) {
		
		PositiveService n = new PositiveService();
		
		//n.addAndReplaceUser("o5wzGvgB1mp_C5vn_a-pyf7UXgio2", "嘉武*_*", "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJhCVV0icRNZ6rE1iaVSUmABkSgRuYhezHQaCLzLm2yjzzBqllH5wLBZpct8RDjrq8bVFiaPouia4ibRCw/132", "o93_pwBL3r17h9GcYhlFqay1oTkk", "-1");		
			
		//n.getShareData("oCQl61ucox8JrHnW5tusoEf4S2XY");
		PPositiveDao positiveDao = new PPositiveDao();
		
		List<P_ReadLog> list = positiveDao.getReadLogByAnaId(254);
		int count = 0;
		for(int i=0;i<list.size();i++) {
			count += list.get(i).getCount();
		}
		
		//根据openId获取设定的阅读倍数
		P_User user = positiveDao.getUserByOpenId("oT_PTv4XRmkwNqzFfYJ1vWkoD6Fc");
		int readNum = user.getReadNum();
		
		if(count%readNum == 0) {
			System.out.println("发送数据");
		}
		
		
	}
	
	
	public String saveUserSet(String openId,String recommend,int recommendTime_m,int recommendTime_n,int recommendTime_e,int clockIn,int eveningPager,int readNum) {
		
		positiveDao = new PPositiveDao();
		
		P_User user = new P_User();
		
		user.setOpenId(openId);
		user.setRecommend(recommend);
		user.setRecommendTime_m(recommendTime_m);
		user.setRecommendTime_n(recommendTime_n);
		user.setRecommendTime_e(recommendTime_e);
		user.setClockIn(clockIn);
		user.setEveningPager(eveningPager);
		user.setReadNum(readNum);
		
		int i = positiveDao.updateUserSet(user);
		
		
		return i>0?"成功":"失败";
	}
	
	public HashMap<String, Object> getUserSet(String openId) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		positiveDao = new PPositiveDao();
		
		String url = Util.articleUrl+"-1";
		
		JSONArray artArray = JSONArray.fromObject(Util.doGet(url).get("data"));
		
		P_User user = positiveDao.getUserByOpenId(openId);
		
		map.put("artArray", artArray);
		
		map.put("user", user);
		
		
		return map;
				
		
	}
	
	
	public int addAna(String openId,int templateId,int templateType,int bindAnaId,String bindOpenId){
		positiveDao = new PPositiveDao();
		P_Ana ana = new P_Ana();
		int andId = 0;
		ana.setOpenId(openId);
		ana.setTemplateId(templateId);
		ana.setTemplateType(templateType);
		ana.setAnaTime(String.valueOf(System.currentTimeMillis()/1000));
		ana.setBindAnaId(bindAnaId);
		ana.setBindOpenId(bindOpenId);
		ana.setColor("white");
		//andId = positiveDao.addAna(ana);
		//防止重复生成的方法，如果后面需要更改问好之类的参数，再来加个更新anaTime的
		P_Ana resultAna = positiveDao.getAnaIdByAna(ana);
		if(resultAna == null){
			andId = positiveDao.addAna(ana);
		}else{
			andId = resultAna.getAnaId();
		}
		
 		return andId;
	}
	
	public String updateReadTimeByReadId(int readId,int readTime) {
		
		positiveDao = new PPositiveDao();
		
		P_ReadLog readLog = new P_ReadLog();
		
		readLog.setReadId(readId);
		
		readLog.setReadTime(String.valueOf(readTime));
		
		int i = positiveDao.updateReadTimeByReadId(readLog);
		
		return i>0?"成功":"失败";
		
	}
	
	public String updateColorByAnaId(int anaId, String color) {
		
		positiveDao = new PPositiveDao();
		
		P_Ana ana = new P_Ana();
		
		ana.setAnaId(anaId);
		ana.setColor(color);
		int i = positiveDao.updateColorByAnaId(ana);
		
		return i>0?"1":"0";
	}
	
	public int addReadLog(long time,String bindOpenId,int anaId, String openId,String nickname,String headimgurl) {
	    
		/**
		 * 推送阅读记录到生成该那篇文章的人
		 */
		PushedService pushedService = new PushedService();
		
		/**
		 * 保存该文章的阅读用户记录
		 */
		positiveDao = new PPositiveDao();
	    
		P_ReadLog readLog = new P_ReadLog();
	    
		readLog.setAnaId(anaId);
	    
		readLog.setOpenId(openId);
	    
		readLog.setNickname(nickname);
		
		readLog.setHeadimgurl(headimgurl);
		
		readLog.setReadTime("0");
		
		int result = positiveDao.addReadLog(readLog);
		
		List<P_ReadLog> list = positiveDao.getReadLogByAnaId(anaId);
		int count = 0;
		for(int i=0;i<list.size();i++) {
			count += list.get(i).getCount();
		}
		
		//根据openId获取设定的阅读倍数
		P_User user = positiveDao.getUserByOpenId(bindOpenId);
		int readNum = user.getReadNum();
		
		//计算阅读总次数和设定的阅读倍数，取余，为0则推送消息
		if(count%readNum == 0) {
			pushedService.pushedReadLog(bindOpenId,time);
		}
		
		
		return result;
	}
	
	
	public void addAndReplaceUser(String openId,String nickname,String headimgurl,String unionId,String bindOpenId) {
		
		positiveDao = new PPositiveDao();
		
		P_User p_User = positiveDao.getUserByOpenId(openId);
		
		if(p_User != null){
			p_User.setOpenId(openId);
			p_User.setNickname(nickname);
			p_User.setHeadimgurl(headimgurl);
			positiveDao.updateUser(p_User);
		}else{
			p_User = new P_User();
			p_User.setOpenId(openId);
			p_User.setNickname(nickname);
			p_User.setHeadimgurl(headimgurl);
			p_User.setUnionId(unionId);
			p_User.setRegTime(String.valueOf(System.currentTimeMillis()/1000));
			p_User.setRecommend(Util.RECOMMEND_DEFAULT);
			p_User.setBindOpenId(bindOpenId);
			positiveDao.addUser(p_User);
		}
		
	}
	
	public HashMap<String, Object> getClientSum(String openId){
		
		HashMap<String, Object> map = new HashMap<>();
		
		positiveDao = new PPositiveDao();
		
		
		List<P_Ana> shareList = positiveDao.getAnaByBindOpenId(openId);
		
		List<P_User> userList = positiveDao.getUserByBindOpenId(openId);
		
		map.put("shareNum", shareList.size());
		map.put("userNum", userList.size());
		
		return map;
		
	}
	
	public List<HashMap<String, Object>> getShareData(String openId) {
	    
		List<HashMap<String, Object>> list = new ArrayList<>();
	    
		positiveDao = new PPositiveDao();
	    
		List<P_Ana> anaList = positiveDao.getAnaByBindOpenId(openId);
		List<P_User> userShare;
		P_Ana ana;
		P_User user;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    
		SimpleDateFormat tm = new SimpleDateFormat("HH:mm:ss");
		
		for (int i = 0; i < anaList.size();) {
	      
			ana = positiveDao.getAnaByAnaId(anaList.get(i).getBindAnaId());
		  
			int readNum = 0;
	      
			HashMap<String, Object> map = new HashMap<>();
		  
			long anaTime = Integer.valueOf(ana.getAnaTime());
	      
			String d = dateFormat.format(new Date(anaTime * 1000));
			
			map.put("anaId", ana.getAnaId());
		  
			if(anaList.get(i).getTemplateType() == 1) {
				  try {
			        String t;
			        if (!belongCalendar(tm.parse(tm.format(new Date(anaTime * 1000L))), tm.parse("06:00:00"), tm.parse("18:00:00"))) {
					  t = "晚安";
					} else {
					  t = "早安";
					} 
			        map.put("aUrl", positivePageUrl);
			        map.put("anaName", "正能量"+ t + d);
			      } catch (ParseException e) {
			        e.printStackTrace();
			      } 
			  }else if(anaList.get(i).getTemplateType() == 2) {
				  map.put("aUrl", eveningPaperPageUrl);
				  map.put("anaName", "电商晚报"+d);
			  }
		  
			userShare = new ArrayList<>();
			for (int j = 0; j < anaList.size(); j++) {
				
				if(ana.getAnaId() == anaList.get(j).getBindAnaId()){
					readNum += 1;
					user = positiveDao.getUserByOpenId(anaList.get(j).getOpenId());
					userShare.add(user);
					//以后删除list中的数据，避免重复,因为删除一条数据，所以j--，此轮循环不++
					anaList.remove(anaList.get(j));
					j--;
				}
			} 
			map.put("readNum", readNum);
			map.put("userShare", userShare);
			list.add(map);
			
	    } 
	    return list;
	}
	
	public List<P_User> getClientDate(String openId){
		
		positiveDao = new PPositiveDao();
		
		List<P_User> userList = positiveDao.getUserByBindOpenId(openId);
		
		return userList;
	}
	
	public List<HashMap<String, Object>> getReadData(String openId) {
	    
		List<HashMap<String, Object>> list = new ArrayList<>();
	    
		positiveDao = new PPositiveDao();
	    
		List<P_Ana> anaList = positiveDao.getAnaByOpenId(openId);
	    
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    
		SimpleDateFormat tm = new SimpleDateFormat("HH:mm:ss");
	    
		for (int i = 0; i < anaList.size(); i++) {
	      
		  int readNum = 0;
	      
		  HashMap<String, Object> map = new HashMap<>();
	      
		  long anaTime = Integer.valueOf(((P_Ana)anaList.get(i)).getAnaTime()).intValue();
	      
		  String d = dateFormat.format(new Date(anaTime * 1000L));
		  map.put("anaId", anaList.get(i).getAnaId());
		  
		  if(anaList.get(i).getTemplateType() == 1) {
			  try {
		        String t;
		        if (!belongCalendar(tm.parse(tm.format(new Date(anaTime * 1000L))), tm.parse("06:00:00"), tm.parse("18:00:00"))) {
				  t = "晚安";
				} else {
				  t = "早安";
				} 
		        map.put("aUrl", positivePageUrl);
		        map.put("anaName", "正能量"+ t + d);
		      } catch (ParseException e) {
		        e.printStackTrace();
		      } 
		  }else if(anaList.get(i).getTemplateType() == 2) {
			  map.put("aUrl", eveningPaperPageUrl);
			  map.put("anaName", "电商晚报"+d);
		  }
		  
		  
		  
		  List<P_ReadLog> userRead = positiveDao.getReadLogByAnaId(anaList.get(i).getAnaId());
	      
		  for (int r = 0; r < userRead.size(); r++) {
	        
			readNum += ((P_ReadLog)userRead.get(r)).getCount();
	        
			userRead.get(r).setUser(positiveDao.getUserByOpenId(userRead.get(r).getOpenId()));
	      } 
	      map.put("readNum", Integer.valueOf(readNum));
	      map.put("userRead", userRead);
	      list.add(map);
	    } 
	    return list;
	}
	
	public List<P_ReadLog> getReadLogByAnaId(int anaId){
		
		positiveDao = new PPositiveDao();
		
		List<P_ReadLog> userRead = positiveDao.getReadLogByAnaId(anaId);
	      
		for (int r = 0; r < userRead.size(); r++) {
	        
	        
			userRead.get(r).setUser(positiveDao.getUserByOpenId(userRead.get(r).getOpenId()));
	    } 
		
		
		return userRead;
	}
	
	public HashMap<String, Object> getMapByAnaId(int anaId){
		
		HashMap<String, Object> map = new HashMap<>();
		positiveDao = new PPositiveDao();
		
		P_Ana ana = positiveDao.getAnaByAnaId(anaId);
		
		P_Template template = positiveDao.getTemplateByTemplateId(ana.getTemplateId());
		
		P_User p_User = positiveDao.getUserByOpenId(ana.getOpenId());
		
		map.put("anaId", ana.getAnaId());
		map.put("openId", ana.getOpenId());
		map.put("color", ana.getColor());
		map.put("templateId", ana.getTemplateId());
		map.put("anaTime", ana.getAnaTime());
		map.put("regTime", p_User.getRegTime());
		map.put("nickname", p_User.getNickname());
		map.put("headimgurl", p_User.getHeadimgurl());
		map.put("content", template.getContent());
		map.put("picUrl", template.getPicUrl());
		
		map.put("Title", "\""+p_User.getNickname()+"\""+"与您的分享 正能量");
		map.put("PhotoCover", template.getPicUrl());
		
		return map;
	}
	
	public HashMap<String, Object> getEveningPagerByAnaId(int anaId){
		
		HashMap<String, Object> map = new HashMap<>();
		positiveDao = new PPositiveDao();
		
		eveningPagerDao = new PEveningPagerDao();
		
		P_Ana ana = positiveDao.getAnaByAnaId(anaId);
		
		P_EveningPager eveningPager = eveningPagerDao.getEveningPaperByPagerId(ana.getTemplateId());
		
		P_User p_User = positiveDao.getUserByOpenId(ana.getOpenId());
		
		map.put("anaId", ana.getAnaId());
		map.put("openId", ana.getOpenId());
		map.put("color", ana.getColor());
		map.put("templateId", ana.getTemplateId());
		map.put("anaTime", ana.getAnaTime());
		map.put("regTime", p_User.getRegTime());
		map.put("nickname", p_User.getNickname());
		map.put("headimgurl", p_User.getHeadimgurl());
		map.put("content", eveningPager.getContent());
		map.put("Title", eveningPager.getPagerTitle());
		
		
		return map;
	}
	
	
	
	public static boolean belongCalendar(Date time, Date beginTime, Date endTime) {
	    
		Calendar date = Calendar.getInstance();
	    
		date.setTime(time);
	    
		Calendar begin = Calendar.getInstance();
	    
		begin.setTime(beginTime);
	    
		Calendar end = Calendar.getInstance();
	    
		end.setTime(endTime);
	    
		if (date.after(begin) && date.before(end)){
	      return true; 
	    }  
	    return false;
    }
	
	/**
	 * 根据当天日期，获取对应模板
	 * @return
	 *//*
	public static int handleTemplateId() {
		positiveDao = new PPositiveDao();
		//获取当天时间戳
		long l = System.currentTimeMillis()/1000;
		//计算当天与系统初始日期相隔的天数
		Long s = (getTimesmorning()+(24*60*60)-templateStartTime)/(24*60*60);
		//获取语录总数，因为分了白天和晚上，所以除以2。
	 	int templateNum = positiveDao.getTemplateCount()/2;
		//用相隔日期取余总数，余数则为anaId
		int templateId = (int) (s%templateNum);
		
		return templateId;
	}
	
	*//**
     * 获取当天0点0分0秒（00:00:00）
     *
     * @return
     *//*
    private static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        //设置当天时间，后面的三个参数是时分秒
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date beginOfDate = cal.getTime();
        
        return beginOfDate.getTime()/1000;
    }*/
	
	
}
