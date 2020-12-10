package com.pgy.wxjssdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;

import com.pgy.common.Util;

import net.sf.json.JSONObject;

public class PositiveWxJsSDK {
	
	private static Logger logger = LogManager.getLogger(PositiveWxJsSDK.class);
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	private static String date = df.format(new Date());
	
	public JSONObject WxJsSDKConfig(String url) throws FileNotFoundException, IOException {
		
		String jsapi_ticket = getJSApiTicket();
		System.out.println("jsapi_ticket:"+jsapi_ticket);
		Map<String, String> ret = PositiveSign.sign(jsapi_ticket, url);
		JSONObject jsonObject = JSONObject.fromObject(ret);
		
		return jsonObject;
	}
	
	//公众号获取openId
	public static JSONObject getWXOpenId(String code){
		
		JSONObject json = null;
												
		StringBuffer getToken = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
		
		getToken.append("appid="+Util.APPID_2);
		getToken.append("&secret="+Util.APPSECRET_2);
		getToken.append("&code="+code);
		getToken.append("&grant_type=authorization_code");
		
		json = Util.doGet(getToken.toString());
		logger.info("获取的access_token数据："+json);
		
		
		
		//StringBuffer getInfo = new StringBuffer("https://api.weixin.qq.com/cgi-bin/user/info?");
		StringBuffer getInfo = new StringBuffer("https://api.weixin.qq.com/sns/userinfo?");
		
		getInfo.append("access_token="+json.get("access_token"));
		getInfo.append("&openid="+json.get("openid")+"&lang=zh_CN");
		
		json = Util.doGet(getInfo.toString());
		logger.info("获取的userinfo数据："+json);
		
		return json;
	}
	
	
	/***
     * 获取jsapiTicket
     * @return
     */
   public static String getJSApiTicket() throws FileNotFoundException, IOException{ 
       //获取token
       String acess_token= getAccessToken();
       String urlStr = //"http://gy7tgr.natappfree.cc//wxapi/weixinMall/index.jsp";
    		   "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+acess_token+"&type=jsapi";  
       JSONObject backData=Util.doGet(urlStr);  
       String ticket = (String) backData.get("ticket");  
       return  ticket;  
          
   }  
   
   
   
   public static String downFileWxImg(String mediaId,String orderId,HttpSession session) throws FileNotFoundException, IOException {
	   
	   String result = "";
	   
	   String accessToken = PositiveWxJsSDK.getAccessToken();
		
	   String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+mediaId;
	   
	   HttpGet httpGet = new HttpGet(url);
	   CloseableHttpClient client = HttpClients.createDefault();
	   HttpResponse resp = client.execute(httpGet);
	   if (resp.getStatusLine().getStatusCode() == 200) {
		    
		    String size = resp.getFirstHeader("Content-Length").getValue();
			
			String contentType = resp.getFirstHeader("Content-Type").getValue();
			
			String fileType = ".jpg";
			if (contentType.equals(MimeTypeUtils.IMAGE_JPEG_VALUE)) {
				fileType = ".jpg";
			} else if (contentType.equals(MimeTypeUtils.IMAGE_PNG_VALUE)) {
				fileType = ".png";
			} else if (contentType.equals(MimeTypeUtils.IMAGE_GIF_VALUE)) {
				fileType = ".gif";
			}
			String filename = UUID.randomUUID().toString() + fileType;
			//最终存储路径：
			
			String path = "/"+Util.SAVECONTENTS+"/"+orderId+"/";
			//File f = new File(path);
			//存到磁盘
			if (!(new File(path)).exists()) {
				//(new File(path)).createNewFile();
				(new File(path)).mkdirs();
			}
			FileCopyUtils.copy(resp.getEntity().getContent(), new FileOutputStream(path+"/"+filename));
			path = "/"+Util.VISITCONTENTS+"/"+orderId+"/";
			result = path+filename;
	   }
	   
	   return result;
   }
   
	/***
     * 获取acess_token 
     * @return
     */
    public static String getAccessToken() throws FileNotFoundException, IOException{
	    String appid = Util.APPID_2;
	    String appSecret = Util.APPSECRET_2;
        String url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret+"";
        JSONObject backData=Util.doGet(url);
        String accessToken = (String) backData.get("access_token");  
        return accessToken;
    }
   
    
    public static Map<String, String> sign(String jsapi_ticket, String url) throws FileNotFoundException, IOException {
	    Map<String, String> ret = new HashMap<String, String>();
	    String nonce_str = create_nonce_str();
	    String timestamp = create_timestamp();
	    String string1;
	    String signature = "";
	        
	 
        //注意这里参数名必须全部小写，且必须有序
	    string1 = "jsapi_ticket=" + jsapi_ticket +
	              "&noncestr=" + nonce_str +
	              "&timestamp=" + timestamp +
	              "&url=" + url;
        System.out.println(string1);
	 
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(string1.getBytes("UTF-8"));
	        signature = byteToHex(crypt.digest());
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch (UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    
	    //自动获取WxTokenUtil.properties中appid属性，避免appid改动时重复更改
	    String appId = Util.APPID_2;
	    
	    
	    ret.put("url", url);
	    //注意这里 要加上自己的appId
	    ret.put("appId", appId);
	    ret.put("jsapi_ticket", jsapi_ticket);
	    ret.put("nonceStr", nonce_str);
	    ret.put("timestamp", timestamp);
	    ret.put("signature", signature);
	 
        return ret;
    }
	 
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
	 
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
	 
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
   /***
    * 模拟get请求
    * @param url
    * @param charset
    * @param timeout
    * @return
    */
    /*public static String sendGet(String url, String charset, int timeout)
     {
       String result = "";
       try
       {
         URL u = new URL(url);
         try
         {
           URLConnection conn = u.openConnection();
           conn.connect();
           conn.setConnectTimeout(timeout);
           BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
           String line="";
           while ((line = in.readLine()) != null)
           {
            
             result = result + line;
           }
           in.close();
         } catch (IOException e) {
           return result;
         }
       }
       catch (MalformedURLException e)
       {
         return result;
       }
      
       return result;
     }*/
	
	
}
