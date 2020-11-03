package com.pgy.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * 工具类
 * @author Administrator
 *
 */


public class Util {
	
	private static Logger logger = LogManager.getLogger(Util.class);
	
	/*
	 * 默认推荐类型参数
	 */
	public static String RECOMMEND_DEFAULT = "0";
	
	/**
	 * 保存照片的目录
	 */									  
	public static String SAVECONTENTS = "D:/javaWeb/photo";
	
	/**
	 * 访问照片的目录
	 */
	public static String VISITCONTENTS = "photo";
	
	/**
	 * 用于计算模板id的时间戳
	 */
	public static long templateStartTime = 1579261199;
	
	/**
	 * 获取文章接口
	 */
	public static String articleUrl = "https://api1.sppxw.com/ywapp/Article/?CID=";
	/**
	 * 获取文章详情接口
	 */
	public static String articleSUrl = "https://api1.sppxw.com/art/Articles/";
	
	public static String APPID = "wx075f8ef021d70cc3";
	
	public static String APPSECRET = "0469079771025674798d260ac0c01bec";
	
	public static String SESSION_OPENID = "session_openId";
	
	public static String SESSION_NICKNAME = "session_nickname";
	
	public static String SESSION_CODE = "session_code";
	
	public static String SESSION_ADMIN = "session_admin";
	
	
	/**
	 * 微信支付商户号
	 */
	public static String MCHID = "1325522601";
	
	public static String WXAPI_KEY = "r5qyllkomweccfk4dan3dchncdzsu5md";
	
	/**
	 * 交易关闭的限定时间
	 */
	public static long PAYCLOSETIME = 24;
	
	
	
	
	public static String getSessionOpenId(HttpSession session) {
		
		//String openId = "o5wzGvgB1mp_C5vn_a-pyf7UXgio";
		
		//String openId = "o93_pwBL3r17h9GcYhlFqay1oTkk";
		
		String openId = (String) session.getAttribute(Util.SESSION_OPENID);
		
		return openId;
	}
	
	public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
            	delFile(f);
            }
        }
        return file.delete();
    }
	
	/**
	 * 生成随机数字和字母  
	 * @param length
	 * @return
	 */
    public static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
	
	/**
	 * 截取字符串
	 * @param str
	 * @param strStart
	 * @param strEnd
	 * @return
	 */
	public static String subString(String str, String strStart, String strEnd) {
		 
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd)+1;
 
        /* index为负数 即表示该字符串中没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :" + str + "中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :" + str + "中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex);
        return result;
    }
	
	
	public static byte[] doPost(String url,HttpEntity body)
	{
		CloseableHttpClient  httpClient = HttpClients.createDefault();
		logger.info("doHttpPost url-->" + url);
		try
		{
			HttpPost request = 
					new HttpPost(url);
			request.setEntity(body);
			RequestConfig requestConfig = RequestConfig.custom()  
				    .setConnectionRequestTimeout(Entry.getInstance().getConfig().getHttpConnectRequestTimeOut())
				    .setConnectTimeout(Entry.getInstance().getConfig().getHttpConnectTimeOut())  
				    .setSocketTimeout(Entry.getInstance().getConfig().getHttpSocketTimeOut()).build();  
			request.addHeader("Connection", "close");
			request.setConfig(requestConfig);
			
			System.out.println("测试测试"+request);
			
				
			
			HttpResponse httpResponse = httpClient.execute(request);
			HttpEntity res = httpResponse.getEntity();
			byte[] ss = EntityUtils.toByteArray(res);
			int status = httpResponse.getStatusLine().getStatusCode();
			
			if (status != 200)
			{
				logger.error("doHttpPost status is not 200 ,status : " + status);
				httpClient.close();
				return null;
			}
			httpClient.close();
			return ss;
		}
		catch (Exception e)
		{
			logger.error("doPost"+"(" + url + ")" + "exception-->" + e);
			
			try
			{
				httpClient.close();
			}
			catch (Exception e1)
			{
				logger.error("close connect,exception-->" + e1);
			}
			return null;
		}
		finally
		{
			try
			{
				httpClient.close();
			}
			catch(Exception e1)
			{
				logger.error("close httpclient,exception-->" + e1);
			}
			
		}
		
	}
	
	
	/**
	 * 浮点数相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(double v1,double v2){
	    BigDecimal b1 = new BigDecimal(Double.toString(v1));
	    BigDecimal b2 = new BigDecimal(Double.toString(v2));
	    return b1.add(b2).doubleValue();
	}
	
	/**
	 * 浮点数相乘
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(double v1,double v2){
	    BigDecimal b1 = new BigDecimal(Double.toString(v1));
	    BigDecimal b2 = new BigDecimal(Double.toString(v2));
	    return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 浮点数相除
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1,double v2){
	    BigDecimal b1 = new BigDecimal(Double.toString(v1));
	    BigDecimal b2 = new BigDecimal(Double.toString(v2));
	    return b1.divide(b2,2).doubleValue();
	}
	
	public static String postFrom(String url,String WxOpenID,File file) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url);
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("WxOpenID", WxOpenID, ContentType.TEXT_PLAIN);
        try {
			builder.addBinaryBody(
			        "imgInfo",
			        new FileInputStream(file),
			        ContentType.APPLICATION_OCTET_STREAM,
			        file.getName()
			    );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        String sResponse = "";
        try {
        	CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            sResponse = EntityUtils.toString(responseEntity, "UTF-8");
		} catch (IOException e) {
			// TODO: handle exception
		}
        logger.info("Post 返回结果"+sResponse);
        
        return sResponse;
	}
	
	public static String sendPost(String url, String param) {
        //PrintWriter out = null;
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Charset", "UTF-8");
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            /*out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            
        	out.print(param);*/
            out = new OutputStreamWriter(conn.getOutputStream(), "Utf-8");
            out.write(param);
            
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	
	/**
	 * 发送get请求获取返回数据
	 * @param str
	 * @return
	 */
	public static JSONObject doGet(String str){
		
		JSONObject json = null;
		
		HttpURLConnection con = null;
		
		try {
			URL url = new URL(str);
			con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setConnectTimeout(5000);
            con.setReadTimeout(50000);
            con.connect();
            //加utf-8编码，避免中文乱码
            InputStreamReader readStream = new InputStreamReader(con.getInputStream(),"utf-8");
            BufferedReader buffer=new BufferedReader(readStream); 
			String data=buffer.readLine();
			
			json = JSONObject.fromObject(data);
			logger.info("get请求返回数据--》"+data);
			readStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (con != null) {
                con.disconnect();
            }
        }
		
		return json;
	}
	
	
	/**
	 * 将时间戳转换为正常时间格式
	 * @return
	 */
	public static String timeSwitch(String reqtime){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(reqtime);
        Date date = null;
        date = new Date(lt);
        String time = simpleDateFormat.format(date);
        return time;
	}
    
	/**
     * 读取图片
     * @param x
     * @param y
     * @param bfi
     * @return
     */
    public static BufferedImage resizeImage(int x, int y, BufferedImage bfi){
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(
                bfi.getScaledInstance(x, y, Image.SCALE_SMOOTH), 0, 0, null);
        return bufferedImage;
    }
	
	
	
	
	
	
	
	
}
