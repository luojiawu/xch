package com.pgy.controller.move;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pgy.common.LunarUtil;
import com.pgy.common.Util;
import com.pgy.dao.PPositiveDao;
import com.pgy.service.PositiveService;
import com.pgy.wxjssdk.PositiveWxJsSDK;
import com.pgy.wxjssdk.WxJsSDK;
import com.pgy.xfUtil.WaveHeader;
import com.pgy.xfUtil.XfTTSWS;
import com.xch.dto.P_Ana;
import com.xch.dto.P_ReadLog;

import net.sf.json.JSONObject;

/**
 * 分享页面
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="p")
public class PositiveController {
	
	private static Logger logger = LogManager.getLogger(PositiveController.class);
	
	private static final String serverUrl = "http://move.ehai.xyz/wx_audio/";
	
	private static final String fileUrl = "D:/javaWeb/wx_audio/";
	
	public static long templateStartTime = 1579261199;
	
	PositiveService positiveService = null;
	
	private static PPositiveDao pPositiveDao = new PPositiveDao();
	
	@RequestMapping(value="/toCreateAna")
	public String toCreateAna() {
		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/createAna?bindOpenId=-1&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
	
	}
	
	@RequestMapping(value="/toCopyAna")
	public String toCopyAna(int bindAnaId,String templateId,String bindOpenId) {
		logger.info("toCopyAna-->bindAnaId:"+bindAnaId+",templateId:"+templateId+",bindOpenId:"+bindOpenId);																		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/copyAna?anaMap="+bindAnaId+","+templateId+","+bindOpenId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
	
	}
	
	@RequestMapping(value="/toClient")
    public String toClient() {
	    return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/client&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
    }
	
	@ResponseBody
	@RequestMapping(value="/getClientData")
	public HashMap<String, Object> getClientData(String openId, int dataType) {
		List<HashMap<String, Object>> dataList;
		HashMap<String, Object> map = new HashMap<>();
		positiveService = new PositiveService();
		switch (dataType) {
		case 1:
			dataList = positiveService.getReadData(openId);
			map.put("dataList", dataList);
			map.put("code", "1");
			break;
		case 2:
			dataList = positiveService.getShareData(openId);
			map.put("dataList", dataList);
			map.put("code", "1");
			break;
		case 3:
			 map.put("dataList", positiveService.getClientDate(openId));
			 map.put("code", "1");
			break;

		default:
			break;
		}
		
		return map;
	}
	
	@RequestMapping(value="/client")
	public String client(String code, Model data) {
		/*String openId = "oCQl61ucox8JrHnW5tusoEf4S2XY";
		data.addAttribute("openId", openId);*/
		String openId = "";
		logger.info("toPositive获取的code数据："+ code);
		if (code == null || code.equals("")){
			return "redirect:toClient";
		}			
		
		JSONObject json = PositiveWxJsSDK.getWXOpenId(code);
		
		openId = (String)json.get("openid");
		
		logger.info("获取的openId："+ openId);
		
		data.addAttribute("openId", openId);
		
		positiveService = new PositiveService();
		
		HashMap<String, Object> map = positiveService.getClientSum(openId);
		
		data.addAttribute("map", map);
		
		return "p_client";
	}
	
	@ResponseBody
	@RequestMapping(value="getReadLogByAnaId")
	public HashMap<String, Object> getReadLogByAnaId(int anaId){
		
		HashMap<String, Object> map = new HashMap<>();
		
		positiveService = new PositiveService();
		
		List<P_ReadLog> userRead = positiveService.getReadLogByAnaId(anaId);
		
		map.put("userRead", userRead);	
		map.put("code", "1");
		
		return map;
				
	}
	
	//用anaid去获取该文章的openId，用于被新用户绑定为bindAnaId
	@RequestMapping(value="toPositive")
	public String toPositive(String code,int anaId,Model data) {
		
		//获取访问用户的openId，与生成语录的openId进行对比
		/*String openId = "oCQl61ucox8JrHnW5tusoEf4S2XY";*/
		String openId = "";
		
		logger.info("toPositive获取的code数据:"+code);
		
		if(code == null || code.equals("")) {
			
			return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/toPositive?anaId="+anaId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
			
		}else {
			
			JSONObject json = PositiveWxJsSDK.getWXOpenId(code);
			openId = (String)json.get("openid");
			logger.info("获取的openId数据:"+openId);
			
		}
		
		
		positiveService = new PositiveService();
		
		HashMap<String, Object> map = positiveService.getMapByAnaId(anaId);
		
		String bindOpenId = (String) map.get("openId");
		logger.info("bindOpenId-->"+bindOpenId+",openId-->"+openId);
		if(bindOpenId.equals(openId)){
			
			data.addAttribute("type", "1");
		}else {
			
			positiveService.addReadLog(anaId, openId);
			data.addAttribute("type", "0");
			
		}
		
		String content = (String) map.get("content");
		long regTime = Integer.valueOf((String)map.get("regTime"));
		long anaTime = Integer.valueOf((String)map.get("anaTime"));
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
		
		
 		String pcm = getMp3((int)newDate.get("month"),(int)newDate.get("day_month"),(String)newDate.get("week"), (LunarUtil)newDate.get("lunar"), content, (String)newDate.get("hello"));
 		
 		data.addAttribute("pcm", serverUrl+pcm+".mp3");
 		
 		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "p_positive";
		
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="setColorByAnaId")
	public String setColorByAnaId(int anaId,String color) {
		
		positiveService = new PositiveService();
		
		String str = positiveService.updateColorByAnaId(anaId, color);
		
		
		return str;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/getJsSDK")
	public JSONObject getJsSDK(HttpServletRequest request,String lUrl) throws FileNotFoundException, IOException {
		
		PositiveWxJsSDK positiveWxJsSDK = new PositiveWxJsSDK();
		
		JSONObject jsonObject = positiveWxJsSDK.WxJsSDKConfig(lUrl);
		
		return jsonObject;
		
	}
	
	
	
	@RequestMapping(value="copyAna")
	public String copyAna(String code,String anaMap){
		
		logger.info("copyAna-->anaMap:"+anaMap);
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
		
		int anaId = positiveService.addAna(openId,templateId,bindAnaId,bindOpenId);
		

		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/toPositive?anaId="+anaId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
		
	}

	
	@RequestMapping(value="createAna")
	public String createAna(String code,String bindOpenId) {
		
		
		positiveService = new PositiveService();
		String openId = "";
		
		logger.info("createAna获取的code数据:"+code);
		
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
		
		
		int anaId = positiveService.addAna(openId,handleTemplateId(),-1,bindOpenId);
		

		
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1e5a15fc07a1433&redirect_uri=http://move.ehai.xyz/photobook/p/toPositive?anaId="+anaId+"&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect";
		
		
	}
	
	public static void main(String[] args) {
		
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
	
	public static String getMp3(int month,int day_month,String week,LunarUtil lunar,String content,String hello){
		
		String text = "今天是"+month+"月"+day_month+"日，"+week+"，农历"+lunar+"，"+content+","+hello;
		String mp3Url = "";
		try {
			mp3Url = XfTTSWS.textToAudio(text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mp3Url;
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
	 * 根据当天日期，获取对应模板
	 * @return
	 */
	public static int handleTemplateId() {
		//获取当天时间戳
		long l = System.currentTimeMillis()/1000;
		//计算当天与系统初始日期相隔的天数
		Long s = (getTimesmorning()+(24*60*60)-templateStartTime)/(24*60*60);
		//获取语录总数，因为分了白天和晚上，所以除以2。
	 	int templateNum = pPositiveDao.getTemplateCount()/2;
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
	
	/**
     * pcm转为MP3
     * @param src 源文件
     * @param target 转换后的文件
     * @throws Exception
     */
    public static void pcmToMp3(String src, String target) throws Exception {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        // 计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader(100);
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.fmtHdrLeth = 16;
        header.bitsPerSample = 16;
        header.channels = 1;
        header.formatTag = 0x0001;
        header.avgBytesPerSec = 8000;
        header.blockAlign = (short)(header.channels * header.bitsPerSample / 8);
        header.avgBytesPerSec = header.blockAlign * header.avgBytesPerSec;
        header.dataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; // WAV标准，头部应该是44字节
        // write header
        fos.write(h, 0, h.length);
        // write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        logger.info("Convert OK!");
    }
}
