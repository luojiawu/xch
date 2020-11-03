package com.pgy.controller.move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class YTDemo {
	
	//电商ID
    private static String user_id="YTOTEST"; 
    
    private static String app_key="sF1Jzn";  
    private static String Secret_Key="1QLlIZ";  
    
    //请求url, 正式环境地址：http://api.kdniao.cc/api/Eorderservice
    private static String ReqURL="http://opentestapi.yto.net.cn/service/waybill_query/v1/rb9JZs";   

    
    public static void main(String[] args) throws Exception {
		
    	String format = "JSON";
    	
    	
    	
    	StringBuffer StrB = new StringBuffer();
    	StrB.append("app_key"+app_key);
    	StrB.append("format"+format);
    	StrB.append("methodyto.Marketing.WaybillTrace");
    	StrB.append("timestamp2020-05-27 11:00:00");
    	StrB.append("user_id"+user_id);
    	StrB.append("v1.01");
    	
    	String signStr = MD5(Secret_Key+StrB.toString(),"UTF-8");
    	
    	Map<String, String> map = new HashMap<>();
    	
    	map.put("sign", signStr);
    	map.put("app_key", app_key);
    	map.put("format", format);
    	map.put("method", "yto.Marketing.WaybillTrace");
    	map.put("timestamp", "2020-05-27 11:00:00");
    	map.put("user_id", user_id);
    	map.put("v", "1.01");
    	map.put("param", "YT9171429861004");
    	
    	String result = sendPost(ReqURL,"sign=6B158AD9E00D8EFCEFBEA8AF50ED611F&app_key=sF1Jzn&format=JSON&method=yto.Marketing.WaybillTrace&timestamp=2020-05-27 15:16:22&user_id=YTOTEST&v=1&param=%5B%7B%22Number%22%3A%22YT9171429861004%22%7D%5D");
    	System.out.println(result);
    	
	}
    
    public static String sendPost(String url, String params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// POST方法
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				StringBuilder param = new StringBuilder();
				/*for (Map.Entry<String, String> entry : params.entrySet()) {
					if(param.length()>0){
						param.append("&");
					}
					param.append(entry.getKey());
					param.append("=");
					param.append(entry.getValue());
				}*/
				out.write(params);
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
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
		return result.toString();
	}
	
    private static String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toUpperCase();
    }

	
}
