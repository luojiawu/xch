package com.pgy.controller.move;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pgy.common.LunarUtil;
import com.pgy.common.Util;
import com.pgy.dao.PEveningPagerDao;
import com.pgy.service.PositiveService;
import com.pgy.service.PushedService;
import com.pgy.wxjssdk.PositiveWxJsSDK;

import net.sf.json.JSONObject;

/**
 * 晚报控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="e")
public class EveningPaperController {
	
	private static Logger logger = LogManager.getLogger(EveningPaperController.class);
	
	
	
	PositiveService positiveService = null;
	
	PushedService pushedService = null;
	
	
	private static PEveningPagerDao eveningPagerDao = new PEveningPagerDao();
	
	@RequestMapping(value="/toCreateEveningPager")
	public String toCreateEveningPager() {
		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Util.APPID+"&redirect_uri=http://move.ehai.xyz/ywPositive/e/createEveningPager?bindOpenId=-1&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
	
	}
	
	@ResponseBody
	@RequestMapping(value="/toCopyEveningPager")
	public String toCopyEveningPager(String openId,int bindAnaId,int templateId,String bindOpenId) {
		logger.info("toCopyEveningPager-->openId"+openId+",bindAnaId:"+bindAnaId+",templateId:"+templateId+",bindOpenId:"+bindOpenId);																		
		
		String aUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Util.APPID+"&redirect_uri=http://move.ehai.xyz/ywPositive/e/copyEveningPager?anaMap="+bindAnaId+","+templateId+","+bindOpenId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
		
		pushedService = new PushedService();
		pushedService.pushedCopyAna(openId,2,aUrl);
		
		return "1";
	
	}
	
	@RequestMapping(value="/toEveningPager")
    public String toEveningPager(int anaId) {
		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Util.APPID+"&redirect_uri=http://move.ehai.xyz/ywPositive/e/eveningPager?anaId="+anaId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
		
	}
	
	
	//用anaid去获取该文章的openId，用于被新用户绑定为bindAnaId
	@RequestMapping(value="eveningPager")
	public String eveningPager(String code,int anaId,Model data) {
		
		//获取访问用户的openId，与生成语录的openId进行对比
		/*String openId = "oCQl61ucox8JrHnW5tusoEf4S2XY";*/
		
		
		logger.info("eveningPager获取的code数据:"+code);
		
		if(code == null || code.equals("")) {
			
			return "redirect:toEveningPager?anaId="+anaId;
		}
		
		JSONObject json = PositiveWxJsSDK.getWXOpenId(code);
		String openId = (String)json.get("openid");
		logger.info("获取的openId数据:"+openId);
		
		
		positiveService = new PositiveService();
		
		HashMap<String, Object> map = positiveService.getEveningPagerByAnaId(anaId);
		
		String bindOpenId = (String) map.get("openId");
		logger.info("bindOpenId-->"+bindOpenId+",openId-->"+openId);
		
		
		long regTime = Integer.valueOf((String)map.get("regTime"));
		long anaTime = Integer.valueOf((String)map.get("anaTime"));
		
		if(bindOpenId.equals(openId)){
			
			data.addAttribute("readId", 0);
			data.addAttribute("type", "1");
		}else {
			String nickname = (String)json.get("nickname");
			String headimgurl = (String)json.get("headimgurl");
			int readId = positiveService.addReadLog(anaTime,bindOpenId,anaId, openId,nickname,headimgurl);
			data.addAttribute("readId", readId);
			data.addAttribute("type", "0");
			
		}
		
		
		data.addAttribute("map", map);
		HashMap<String, Object> newDate = new HashMap<>();
		try {
			newDate = getNewDate(anaTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.addAttribute("numberDay", handleRegDay(regTime,anaTime));
 		data.addAttribute("newDate", newDate);
 		data.addAttribute("openId", openId);
 		
 		
		return "p_eveningpaper";
		
	}
	
	
	
	
	
	
	@RequestMapping(value="copyEveningPager")
	public String copyEveningPager(String code,String anaMap){
		
		logger.info("copyEveningPager-->anaMap:"+anaMap);
		String[] anaMaps = anaMap.split(",");
		
		int bindAnaId = Integer.valueOf(anaMaps[0]);
		int templateId = Integer.valueOf(anaMaps[1]);
		String bindOpenId = anaMaps[2];
		
		logger.info("copyAna-->bindAnaId:"+bindAnaId+",templateId:"+templateId+",bindOpenId:"+bindOpenId);
		positiveService = new PositiveService();
		String openId = "";
		
		logger.info("copyAna获取的code数据:"+code);
		
		if(code == null || code.equals("")) {
			return "redirect:toCreateAna";
			
		}else {
			
			JSONObject json = PositiveWxJsSDK.getWXOpenId(code);
			openId = (String)json.get("openid");
			String nickname = (String)json.get("nickname");
			String headimgurl = (String)json.get("headimgurl");
			String unionId = (String)json.get("unionid");
			
			
			positiveService.addAndReplaceUser(openId,nickname,headimgurl,unionId,bindOpenId);
			logger.info("获取的openId数据:"+openId);
			
		}
		
		int anaId = positiveService.addAna(openId,templateId,2,bindAnaId,bindOpenId);
		
		/**
		 * 调用推送消息接口，返回复制文章结果
		 */

		
		return "redirect:http://move.ehai.xyz/ywPositive/e/toEveningPager?anaId="+anaId;
		
	}

	
	@RequestMapping(value="createEveningPager")
	public String createEveningPager(String code,String bindOpenId) {
		
		
		positiveService = new PositiveService();
		String openId = "";
		
		logger.info("createEveningPager获取的code数据:"+code);
		
		if(code == null || code.equals("")) {
			return "redirect:toCreateEveningPager";
			
		}else {
			
			JSONObject json = PositiveWxJsSDK.getWXOpenId(code);
			openId = (String)json.get("openid");
			
			if(openId == null || openId.equals("")) {
				return "redirect:toCreateEveningPager";
				
			}
			
			String nickname = (String)json.get("nickname");
			String headimgurl = (String)json.get("headimgurl");
			String unionId = (String)json.get("unionid");
			
			
			positiveService.addAndReplaceUser(openId,nickname,headimgurl,unionId,bindOpenId);
			logger.info("获取的openId数据:"+openId);
			
		}
		
		
		int anaId = positiveService.addAna(openId,pagerId(),2,-1,bindOpenId);
		

		
		return "redirect:http://move.ehai.xyz/ywPositive/e/toEveningPager?anaId="+anaId;
		
		
	}
	
	
	/**
     * 获取时间方面的数据
     *
     * @return
     */
	private static HashMap<String, Object> getNewDate(long time) throws ParseException {
		
    	HashMap<String, Object> map = new HashMap<>();
    	String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	//如果传进来的时间戳不等于空，则直接用于转换时间
    	if(!"".equals(time)) {
    		date = new Date(time*1000);
		}
    	
    	String d = dateFormat.format(date);
        Calendar today = Calendar.getInstance();
        try {
			today.setTime(dateFormat.parse(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        int week_index = today.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		} 
		
        
		
		
		LunarUtil lunar = new LunarUtil(today);
        
		
		map.put("month", today.get(Calendar.MONTH)+1);
		map.put("day_month", today.get(Calendar.DAY_OF_MONTH));
        map.put("week", weeks[week_index]);
        map.put("day", d);
        map.put("lunar", lunar);
        
        SimpleDateFormat tm = new SimpleDateFormat("HH:mm:ss");
        
        try {
			if(belongCalendar(tm.parse(tm.format(date)), tm.parse("04:00:00"), tm.parse("10:30:59"))) {
				map.put("hello","朋友,早安");
				map.put("helloSpan","早 / 安");
			}else if(belongCalendar(tm.parse(tm.format(date)), tm.parse("10:31:00"), tm.parse("13:59:59")) || belongCalendar(tm.parse(tm.format(date)), tm.parse("17:31:00"), tm.parse("20:59:59"))){
				map.put("hello", "朋友,加油");
				map.put("helloSpan","加 / 油");
			}else if(belongCalendar(tm.parse(tm.format(date)), tm.parse("14:00:00"), tm.parse("17:30:59"))) {
				map.put("hello", "朋友,下午好");
				map.put("helloSpan","午 / 安");
			}else if(belongCalendar(tm.parse(tm.format(date)), tm.parse("21:00:00"), tm.parse("23:59:59")) || belongCalendar(tm.parse(tm.format(date)), tm.parse("00:00:00"), tm.parse("03:59:59"))) {
				map.put("hello", "朋友,晚安");
				map.put("helloSpan","晚 / 安");
			}
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        return map;
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
	
	
	
	 
	
	/**
	 * 根据当天日期，获取对应模板
	 * @return
	 */
	public static int pagerId() {
		
		
	 	int pagerId = eveningPagerDao.getEveningpaperId();
		
		
		return pagerId;
	}
	
	/**
	 * 根据当天日期，获取注册天数
	 * @return
	 */
	public static long handleRegDay (long regTime,long anaTime) {
		
		//计算当天与系统初始日期相隔的天数
		Calendar cal = Calendar.getInstance();
        //设置文章时间，后面的三个参数是时分秒
		cal.setTime(new Date(anaTime*1000));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        Date beginOfDate = cal.getTime();
        
        long l = beginOfDate.getTime()/1000;
		
		Long s = (l+(24*60*60)-regTime)/(24*60*60);
		
		return s;
	}
}
