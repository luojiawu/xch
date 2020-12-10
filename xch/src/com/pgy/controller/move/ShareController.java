package com.pgy.controller.move;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgy.common.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 分享页面
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="s")
public class ShareController {
	
	private static Logger logger = LogManager.getLogger(ShareController.class);
	
	/**
	 * 消息推送token
	 */
	private static String token = "pgyAppletNotify";
	/**
	 * 消息推送EncodingAESKey密钥  需要对消息进行加密时使用，暂不使用
	 */
	//private static String EncodingAESKey = "Vq0uZNBgzQoel9st8zRh8HQP09XuwPOyGXtVbJzXT7g";
	
	
	/**
	 * 获取文章接口
	 */
	private static String ArtUrl = "https://api1.sppxw.com/Public/Art/?ArtID=";
	
	/**
	 * 获取用户文章列表接口
	 */
	private static String ArtListUrl = "https://api1.sppxw.com/ywapp/Article/Cli/";
	/**
	 * 分享页面
	 */
	private static String shareUrl = "https://move.ehai.xyz/photobook/s/toShare?ArtID=";
	/**
	 * 发送信息至客服接口
	 */									
	private static String customerUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send"; 
	/**
	 * 获取token接口
	 */				
	private static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
	/**
	 * 本地icon保存地址
	 */
	private static String iconUrl = "D:/javaWeb/custom/common/ShareIcon/";
	
	private static String picUrl = "https://move.ehai.xyz/custom/common/ShareIcon/";
	
	
	@RequestMapping(value="toShare")
	public String toShare(String ArtID,Model data) {
		
		HashMap<String, Object> map = getArticle(ArtID);
		
		data.addAttribute("map", map);
		
		return "s_share";
		
	}
	
	
	public static String getPicUrl(String url) throws IOException {
		
		String[] urls = url.split("/");
		String urlName = iconUrl+urls[urls.length-1];
		BufferedImage background = ImageIO.read(getImageStream(url));
		ImageIO.write(background, "jpg", new File(urlName));
		int width = background.getWidth();
		int height = background.getHeight();
		int iconWidth = width;
		//如果高大于宽，则计算并截取出中间的部分
		if(height > width) {
			
			int i = height-width;
			i = i/2;
			cut(new File(urlName), new File(urlName), 0, i, width, width);
			
		}else {
			
			int i = width-height;
			i = i/2;
			cut(new File(urlName), new File(urlName), i, 0, height, height);
			iconWidth = height;
		}
		
		
		background = ImageIO.read(new File(urlName));
		BufferedImage background_icon = ImageIO.read(new File("D:\\javaWeb\\custom\\common\\icon\\4i8N5h9eI5w3OGp1.png"));
		
		
		
		Graphics2D g = background.createGraphics();
		g.drawImage(background_icon.getScaledInstance(iconWidth/5,iconWidth/5, Image.SCALE_DEFAULT), (iconWidth-iconWidth/5)-20, (iconWidth-iconWidth/5)-20, null);
		g.dispose();
		ImageIO.write(background, "jpg", new File(urlName));
		
		return picUrl+urls[urls.length-1];
		
	}
	
	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 * @throws IOException
	 */
	public static void cut(File srcImageFile, File result, int x, int y,
			int width, int height) {
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(srcImageFile);
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
			ImageReader reader = (ImageReader) iterator.next();
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rectangle = new Rectangle(x, y, width, height);
			param.setSourceRegion(rectangle);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg", result);
		} catch (Exception e) {
			logger.error("图片操作失败", e);
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					logger.error("文件关闭失败", e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		
		
		getArticle("4297");
	}
	
	/**
	 * 调用忆我文书后台接口，获取相关数据并拼成前端使用的HashMap
	 * @param WxOpenID
	 * @param ArtID
	 * @return
	 */
	public static HashMap<String, Object> getArticle(String ArtID){
		
		HashMap<String, Object> map = new HashMap<>();
		
		List<Object> contentList = new ArrayList<Object>();
		
		List<Object> artList = new ArrayList<Object>();
		
		JSONObject jsonObject = null;
		
		JSONArray dataArray = null;
		JSONArray artArray = null;
		JSONObject artJson = null;
		JSONObject dataJson = null;
		JSONObject userJson = null;
		
		String UserID;
		
		String url = ArtUrl+ArtID;
		
		artJson = Util.doGet(url);
		
		String msg = (String)artJson.get("msg");
		
		if("success".equals(msg)) {
			
			/**
			 * 处理文章数据
			 */
			dataArray = JSONArray.fromObject(artJson.get("data"));
			dataJson = (JSONObject) dataArray.get(0);
			
			UserID = dataJson.getString("UserID");
			
			map.put("Title", dataJson.get("Title"));
			map.put("ReadCount", dataJson.get("ReadCount"));
			try {
				
				map.put("PhotoCover", getPicUrl((String)dataJson.get("PhotoCover")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			map.put("TimeCreate", dataJson.get("TimeCreate"));
			map.put("FontType", dataJson.get("FontType"));
			map.put("FontSize", dataJson.get("FontSize"));
			
			JSONArray jsonArray = JSONArray.fromObject(dataJson.get("Content"));
			
			for(int i = 0;i < jsonArray.size(); i++) {
	            
				jsonObject = jsonArray.getJSONObject(i);
				
				jsonObject.put("text", jsonObject.getString("text").replace("---", "\n"));
				
	            contentList.add(JSONObject.toBean(jsonObject));
	        
			}
			map.put("Content", contentList);
			
			
			map.put("UserName", dataJson.get("UserName"));
			map.put("WxHeadImgUrl", dataJson.get("WxHeadImgUrl"));
			
			/**
			 * 获取文章列表
			 */
			UserID = dataJson.getString("UserID");
			url = ArtListUrl+"?UserID="+UserID+"&PageNo=1&PageSize=2";
			//url = ArtListUrl+"?UserID="+UserID+"&PageNo=1&PageSize=2";
			
			
			artArray = JSONArray.fromObject(Util.doGet(url).get("data"));
			int artSize = artArray.size()>2?2:artArray.size();
			for(int i=0;i < artSize;i++) {
				
				jsonObject = artArray.getJSONObject(i);
				artList.add(JSONObject.toBean(jsonObject));
				
			}
			map.put("artList", artList);
			
			
		}
		
		return map;
	}
	
	
	/**
	 * 获取消息推送接口，开启服务器消息推送配置时使用，平时注释掉
	 * @param request
	 * @param response
	 */
	/*@RequestMapping(value="/appletNotify")
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("客服结果通知");
		
		String WxOpenID = "";
		
		String[] ArtID = null;
		
		String picurl = "";
		
		try {
		
			InputStream inStream = request.getInputStream();
			*//**
			 * 获取get请求的参数，生成sign与微信发来的比较，正确则继续执行后面的代码
			 *//*
			String get = request.getQueryString();
	        logger.info("小程序客服的数据："+get);
	        String[] gets = get.split("&");
			
			String signature = gets[0].split("signature=")[1];
			String echostr = gets[1].split("echostr=")[1];
			String timestamp = gets[2].split("timestamp=")[1];
			String nonce = gets[3].split("nonce=")[1];
			
			
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
	        	logger.info("echostr："+echostr);
	        	response.getWriter().write(echostr);
	        	
	        }
	        
	        
	        
			
		} catch (Exception e) {
			
	        try {
	        	response.getWriter().write("success");
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
		}
		
	}*/
	
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
	
	
	/**
	 * 设置发送消息的格式数据
	 * @param WxOpenID openId
	 * @param ArtID 忆我文书的文章id
	 * @param picurl
	 * @return
	 */
	public static JSONObject setContent(String WxOpenID,String ArtID,String picurl) {
		
		
		JSONObject link = new JSONObject();
		JSONObject json = new JSONObject();
		
		String url = shareUrl+ArtID;
		
		link.put("title", "点击进入，即可分享朋友圈");
		link.put("description", "打开页面，点击右上角【…】，分享到朋友圈");
		link.put("url", url);
		link.put("thumb_url", picurl);
		json.put("touser", WxOpenID);
		json.put("msgtype", "link");
		json.put("link", link);
		
		return json;
	}
	
	/**
	 * 获取token
	 * @return
	 */
	public static String getToKen() {
		//忆我文书小程序的appid和secret
		String appid = "wx1e285e75d64acc56";
		
		String secret = "3cde665a52a8d62fe0aadd30bc133548";
		
		String url = tokenUrl+"?grant_type=client_credential&appid="+appid+"&secret="+secret;
		
		JSONObject jsonObject = Util.doGet(url);
		
		return (String) jsonObject.get("access_token");
		
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

    
    
    public static InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return null;
    }
    
}
