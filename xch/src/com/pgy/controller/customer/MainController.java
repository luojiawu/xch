package com.pgy.controller.customer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pgy.common.FileToZip;
import com.pgy.common.RecodeUtil;
import com.pgy.common.Util;
import com.pgy.imageapi.Sample;
import com.pgy.service.MainService;
import com.pgy.wxjssdk.WxJsSDK;
import com.pgy.wxpaysdk.WXPay;
import com.pgy.wxpaysdk.WXPayConfigImpl;
import com.pgy.wxpaysdk.WXPayConstants.SignType;
import com.xch.dto.Site;
import com.pgy.wxpaysdk.WXPayUtil;

import net.sf.json.JSONObject;

/**
 * 管理控制器
 * @author Administrator
 *
 */

@Controller				 
@RequestMapping(value="/main")
public class MainController {
	
	private static Logger logger = LogManager.getLogger(MainController.class);
	
	MainService mainService = new MainService();
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	private static String date = df.format(new Date());
	
	
	WXPayConfigImpl config;
	
	WXPay wxpay;
	
	public MainController() throws Exception {
        config = WXPayConfigImpl.getInstance();
        wxpay = new WXPay(config);
        /*total_fee = "1";
        // out_trade_no = "201701017496748980290321";
        out_trade_no = "201613091059590000003433-axx458";*/
    }
	
	@ResponseBody
	@RequestMapping(value="/getJsSDK")
	public JSONObject getJsSDK(HttpServletRequest request,String lUrl) throws FileNotFoundException, IOException {
		
		//StringBuffer url = request.getRequestURL();
		
		/*String url = "http://move.ehai.xyz/photobook/page/toPhotoPage";*/
		
		//System.out.println("lUrl->>"+lUrl);
		
		WxJsSDK wxJsSDK = new WxJsSDK();
		
		JSONObject jsonObject = wxJsSDK.WxJsSDKConfig(lUrl);
		
		return jsonObject;
		
	}
	
	
	
	@RequestMapping(value="/wxNotify")
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("微信支付结果通知");
		String return_code =null;
		String out_trade_no = null;
		double payMoney = 0;
	    String attach =null;
	    Map<String,String> result = new HashMap<>();
	    String xml = null;
		
		try {
		
			InputStream inStream = request.getInputStream();
	        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = inStream.read(buffer)) != -1) {
	            outSteam.write(buffer, 0, len);
	        }
	        outSteam.close();
	        inStream.close();
	        String resultStr  = new String(outSteam.toByteArray(),"utf-8");
	        logger.info("支付成功的回调xml："+resultStr);
	        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultStr);
	        logger.info("支付成功的回调map："+resultStr);
	        
	        return_code = (String) resultMap.get("return_code");
	        
	        if(return_code.equals("SUCCESS")){
	        	//微信传过来的金额是以分为单位，计算后转为元
	        	payMoney =  Util.divide(Integer.parseInt((String) resultMap.get("total_fee")), 100);
	        	
		        attach = (String) resultMap.get("attach");
		        out_trade_no = (String) resultMap.get("out_trade_no");
		        
		        
		        JSONObject json = JSONObject.fromObject(attach);  
				
		        String orderId = (String) json.get("orderId");
				
				int goodsNum = Integer.parseInt((String)json.get("goodsNum"));
				String payTime = String.valueOf(System.currentTimeMillis());
				String str = mainService.updateOrderStatus(orderId, out_trade_no, payMoney, goodsNum, payTime);
				logger.info("订单操作的结果---->"+str);
				
				//如果返回结果不是1表示订单操作不成功，返回给微信FALL让微信重发
		    	if(!str.equals("1")){
		    		result.put("return_code", "FAIL");
			        
			        xml = WXPayUtil.mapToXml(result);
			        logger.info("返回微信数据："+xml);
		            response.getWriter().write(xml);
		    	}
		        
		    }
	        
	        result.put("return_code", "SUCCESS");
			xml = WXPayUtil.mapToXml(result);
			logger.info("返回微信数据："+xml);
	        //通知微信.异步确认成功.必写.不然微信会一直通知后台.八次之后就认为交易失败了.
	        response.getWriter().write(xml);
	        
	        
			
		} catch (Exception e) {
			
			logger.info("微信回调接口出现错误："+e);
	        try {
	        	result.put("return_code", "FAIL");
		        
		        xml = WXPayUtil.mapToXml(result);
		        logger.info("返回微信数据："+xml);
	            response.getWriter().write(xml);
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="/delSiteBySiteId")
	public String delSiteBySiteId(HttpSession session,int siteId) {
		
		String openId = Util.getSessionOpenId(session);
		
		
		String s = mainService.delSiteBySiteId(siteId,openId);
		
		
		return s;
	}
	
	@ResponseBody
	@RequestMapping(value="/pickOrderSiteId")
	public String pickOrderSiteId(HttpSession session,int siteId,String orderId) {
		
		String s = mainService.pickOrderSiteId(orderId, siteId);
		
		
		
		
		return s;
	}
	
	@ResponseBody			
	@RequestMapping(value="/addAndReplaceSite")
	public String addAndReplaceSite(HttpSession session,int siteId,String name,String phone,String site,int defaultStatus,String orderId,int checkedSiteId) {
		Site s = new Site();
		if(0 != siteId) {
			s.setSiteId(siteId);
		}
		s.setName(name);
		s.setPhone(phone);
		s.setSite(site);
		
		String openId = Util.getSessionOpenId(session);
		String result = mainService.addAndReplaceSite(openId,s, defaultStatus,orderId,checkedSiteId);
		
		return result;
	}
	
	
	
	/**
	 * 上传照片并生成订单的接口
	 * @param fileInput 照片数组
	 * @param goodsId 商品id
	 * @param typeId 类型id
	 * @param defaultPaper 默认张数，用于计算价格
	 * @param session
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="/uploadPhoto")
	public String uploadPhoto(String fileInput[],int goodsId,int typeId,HttpSession session) throws FileNotFoundException, IOException {
		
		List<String> list = new ArrayList<String>();
		
		String openId = Util.getSessionOpenId(session);
		
		String orderId = openId.substring(6,10)+String.valueOf((int)((Math.random()*9+1)*10000000));
		
		if (fileInput != null && fileInput.length > 0) {
			for (int i = 0; i < fileInput.length; i++) {
				
				String mediaId = fileInput[i];
				try {
					
					list.add(WxJsSDK.downFileWxImg(mediaId,orderId,session));
					
					//list.add(FileUpload.imgsUpload(file, session, "/"+Util.CONTENTS+"/"+date+"/"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		int i = mainService.placeOrder(list, orderId, openId, goodsId, typeId);
		
		if(i!=1) {
			orderId = "error";
		}
		
		return orderId;
	}
	
	@ResponseBody
	@RequestMapping(value="/delOrderByOrderId")
	public String delOrderByOrderId(String orderId) {
		int i = mainService.delOrderByOrderId(orderId);
		
		return i>0?"1":"0";
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value="/getPrepayId")
	public Map<String,String> getPrepayId(HttpServletRequest request,HttpSession session,String orderId,int goodsNum) {
		
		HashMap<String, Object> m = mainService.getMoneyByOrderId(orderId);
		
		double payMoney = Util.add(Util.multiply((double)m.get("money"), goodsNum), (double)m.get("postage")); 
		
		Integer wxMoney =  (int) Util.multiply(payMoney, 100);
		
		logger.info("订单金额-->"+wxMoney);
		String openId = "ojgin5DhC_CU5V4HpRPbjG0bArZ4";
		//String openId = Util.getSessionOpenId(session);
						 
		
		logger.info("openId-->"+openId);
		HashMap<String, String> data = new HashMap<>();
		
		String body = "忆我相册书";
		data.put("body", body);
        data.put("fee_type", "CNY");
        data.put("spbill_create_ip", request.getRemoteAddr());
        
        data.put("notify_url",Util.WXNOTIFY_URL);
        
        //data.put("trade_type", "JSAPI");
        data.put("trade_type", "MWEB");
        data.put("total_fee", String.valueOf(wxMoney));
        //动态生成
        String out_trade_no = "WX"+openId.substring(6,10)+String.valueOf((int)((Math.random()*9+1)*100000000));
        data.put("out_trade_no",out_trade_no);
        
        JSONObject json = new JSONObject();
        json.put("goodsNum", String.valueOf(goodsNum));
        json.put("orderId", orderId);
        
        data.put("attach", json.toString());
		
        logger.info("微信订单数据:"+data);
        
		Map<String, String> map = new HashMap<>();
		
		
		try {
		
			Map<String, String> resultMap = wxpay.unifiedOrder(data);
			
			logger.info("微信统一下单返回数据:"+resultMap.toString());
			
			String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
			map.put("appId", Util.APPID);			
            map.put("nonceStr", WXPayUtil.generateUUID());
            map.put("timeStamp",timeStamp);
            map.put("signType", "MD5");
            
            map.put("package", "prepay_id="+resultMap.get("prepay_id"));
            
            String paySign = WXPayUtil.generateSignature(map, Util.WXAPI_KEY, SignType.MD5);
            //上面的package是用于签名，prepay_id用于传到页面使用
            map.put("prepay_id", "prepay_id="+resultMap.get("prepay_id"));
            map.put("paySign", paySign);
            
        }catch(Exception e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	@RequestMapping(value="/barCode")
 	public void barCode(HttpServletRequest request,@RequestParam(value = "files") MultipartFile files,HttpServletResponse response) {
		String sourceName = files.getOriginalFilename().replace(".txt", ""); // 原始文件名
        //String fileType = sourceName.substring(sourceName.lastIndexOf("."));
        

        // 存放文件临时路径
        String uploadBase = "D:/upload";
        String barCodeBase = "D:/barCode";
        
        delAllFile(uploadBase);
        delAllFile(barCodeBase);
        String path = uploadBase + File.separator + sourceName;
        File upload = new File(path);
        try {
            files.transferTo(upload);
        } catch (IOException e) {
        	logger.error("文件上传失败");
        }
        
        String barCodes = txt2String(upload);
 		
        
        
 		String[] str = barCodes.split("\r\n");
 		for(int i=1;i<str.length;i++) {
 			RecodeUtil.barCode(str[i],200,50,barCodeBase+"/code128_"+str[i]+".png");
 			
 		}
        
 		try {  
            //调用FileToZip接口生成压缩包
           boolean flag = FileToZip.fileToZip(barCodeBase, uploadBase, sourceName);
           if(flag){  
          	   logger.info("文件打包成功!");
           }else{  
          	 logger.info("文件打包失败!");  
           }  
           String fileName = sourceName+".zip";
           //Zip压缩包路径
           String filePath = uploadBase+"/"+fileName;  
           File file = new File(filePath);  
           if(file.length()<1||file==null){
          	  logger.info("文件不存在！");  
           }else{
               response.setCharacterEncoding("UTF-8");  
               response.setHeader("Content-Disposition",  
                       "attachment; filename=" + new String(fileName.getBytes("ISO8859-1"), "UTF-8"));  
               response.setContentLength((int) file.length());  
               response.setContentType("application/zip");// 定义输出类型  
               FileInputStream fis = new FileInputStream(file);  
               BufferedInputStream buff = new BufferedInputStream(fis);  
               byte[] b = new byte[1024];// 相当于我们的缓存  
               long k = 0;// 该值用于计算当前实际下载了多少字节  
               OutputStream myout = response.getOutputStream();// 从response对象中得到输出流,准备下载  
               // 开始循环下载  
               while (k < file.length()) {  
                   int j = buff.read(b, 0, 1024);  
                   k += j;  
                   myout.write(b, 0, j);  
               }  
               // 刷新此输出流并强制将所有缓冲的输出字节被写出
                   myout.flush();  
                   myout.close();
                   buff.close(); 
                   fis.close();
                   file.delete();  
           }
       } catch (Exception e) {  
           e.printStackTrace();
       }  
 		
        
        
 	}
	
	 @RequestMapping(value="/downloadPhotoBook")
     public void downloadPhotoBook(String orderId,int type,HttpServletRequest request,HttpServletResponse response) {
		 String filePath = Util.SAVECONTENTS+"/"+orderId;
		 if(type == 2) {
			 filePath = "D:/javaWeb/ywPhoto/"+orderId;
		 }
		 
		   
		 
		 
		 
         try {  
              //调用FileToZip接口生成压缩包
             boolean flag = FileToZip.fileToZip(filePath, filePath, orderId);  
             if(flag){  
            	 logger.info("文件打包成功!");
                 System.out.println("文件打包成功!");  
             }else{  
            	 logger.info("文件打包失败!");  
             }  
             String fileName = orderId+".zip";  
             //Zip压缩包路径
             String path = filePath+"/"+fileName;  
             File file = new File(path);  
             if(file.length()<1||file==null){
            	  logger.info("文件不存在！");  
             }else{
                 response.setCharacterEncoding("UTF-8");  
                 response.setHeader("Content-Disposition",  
                         "attachment; filename=" + new String(fileName.getBytes("ISO8859-1"), "UTF-8"));  
                 response.setContentLength((int) file.length());  
                 response.setContentType("application/zip");// 定义输出类型  
                 FileInputStream fis = new FileInputStream(file);  
                 BufferedInputStream buff = new BufferedInputStream(fis);  
                 byte[] b = new byte[1024];// 相当于我们的缓存  
                 long k = 0;// 该值用于计算当前实际下载了多少字节  
                 OutputStream myout = response.getOutputStream();// 从response对象中得到输出流,准备下载  
                 // 开始循环下载  
                 while (k < file.length()) {  
                     int j = buff.read(b, 0, 1024);  
                     k += j;  
                     myout.write(b, 0, j);  
                 }  
                 // 刷新此输出流并强制将所有缓冲的输出字节被写出
                     myout.flush();  
                     myout.close();
                     buff.close(); 
                     fis.close();
                     file.delete();  
             }
             
            //删除项目文件夹下所有的图片
             /*File filedel = new File(filePath);  
            if (filedel.isDirectory()) { //如果path表示的是一个目录
                   
                 File[] fileList = filedel.listFiles();  
                 for (int i = 0; i < fileList.length; i++) {  
                     File delfile = fileList[i];  
                     if (!delfile.isDirectory()) {      //如果文件的不是一个目录，则删除    
                         //删除文件
                          delfile.delete();                             
                     } 
                 }  
             } */
         } catch (Exception e) {  
             e.printStackTrace();
         }  
            
	 }        
	
	 
	 
	 @ResponseBody
	 @RequestMapping("/imageSearch")
	 public String imageSearch(String mediaId,HttpSession session) throws IOException {
		 
		 String imageSearch = "imageSearch";
		 
		 Sample sample = new Sample();
		
		 String imageUrl = WxJsSDK.downFileWxImg(mediaId,imageSearch,session);
		 
		 String result = sample.SearchSamePhoto("D://javaWeb/"+imageUrl);
		 
		 return result;
	 }
	 
	 /**
     * 生成微信图片二维码
     *
     * @param request
     * @param response
     * @param content   为前端传过来的二维码的内容，即路径链接
     * @throws Exception
     */
 	@RequestMapping("/qrcode")
    public void qrcode(HttpServletResponse response, @RequestParam(name = "content") String content) throws Exception {
        //调用工具类，生成二维码   
        RecodeUtil.creatRrCode(content, 180,180,response);   //180为图片高度和宽度
    }
 	
 	
	
 	public static boolean delAllFile(String path) {  
        boolean flag = false;  
        File file = new File(path);  
        if (!file.exists()) {  
          return flag;  
        }  
        if (!file.isDirectory()) {  
          return flag;  
        }  
        String[] tempList = file.list();  
        File temp = null;  
        for (int i = 0; i < tempList.length; i++) {  
           if (path.endsWith(File.separator)) {  
              temp = new File(path + tempList[i]);  
           } else {  
               temp = new File(path + File.separator + tempList[i]);  
           }  
           if (temp.isFile()) {  
              temp.delete();  
           }  
           if (temp.isDirectory()) {  
              delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件  
              
              flag = true;  
           }  
        }  
        return flag;  
      }  
 	 
 	 /**
 	  * 读取文件内容转成String
 	  * @param file
 	  * @return
 	  */
 	 public static String txt2String(File file){
         StringBuilder result = new StringBuilder();
         try{
        	 InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
             BufferedReader br = new BufferedReader(isr);//构造一个BufferedReader类来读取文件
             String s = null;
             while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	 if(!"".equals(s) && null != s) {
            		 //result.append(System.lineSeparator()+s);
            		 result.append(s+System.lineSeparator());
            	 }
                 
             }
             br.close();    
         }catch(Exception e){
             e.printStackTrace();
         }
         return result.toString();
     }
 	
 	public void wenzhangquchongpaixu() throws IOException {
 		
 		List<String> list = new ArrayList<>();
 		
 		FileWriter fileWriter = new FileWriter("F:/a.txt");
 		
 		String file = txt2String(new File("F:/b.txt"));
 		
 		String[] s = file.split("\r\n");
 		
 		for(int i=0;i<s.length;i++) {
 			
 			
 			
 			String str = s[i].split("\\.")[1];
 			
 			for(int j=0;j<list.size();j++) {
 				
 				if(list.get(j).equals(str)) {
 					list.remove(str);
 					System.out.println("删除重复句子："+str);
 					
 				}
 	 			
 			}
 			list.add(str);
 			
 		}
 		
 		int w = 1;
 		for(int q=0;q<list.size();q++) {
 			if(w == 101) {
 				w = 1;
 			}
 			fileWriter.write((q+1)+"."+list.get(q)+"\r\n");//写入 \r\n换行
 			w++;
 		}
 		
 		
		fileWriter.flush();
		fileWriter.close();
 		
 	} 
 	 
 	public static String generateRandomFilename(){ 
 		String RandomFilename = ""; 
 		Random rand = new Random();//生成随机数 
 		int random = rand.nextInt(); 

 		Calendar calCurrent = Calendar.getInstance(); 
 		int intDay = calCurrent.get(Calendar.DATE); 
 		int intMonth = calCurrent.get(Calendar.MONTH) + 1; 
 		int intYear = calCurrent.get(Calendar.YEAR); 
 		String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" + 
 		String.valueOf(intDay) + "_"; 

 		RandomFilename = now + String.valueOf(random > 0 ? random : ( -1) * random); 

 		return RandomFilename; 
	}
 	
 	public static void main(String[] args) throws IOException {
 		FileWriter fileWriter = new FileWriter("F:/b.txt");
 		File folder = new File("F:/图片");
		//获取该目录下所有文件的File数组
		File[] fileArray = folder.listFiles();
		
		String file = txt2String(new File("F:/a.txt"));
 		
 		String[] s = file.split("\r\n");
 		
 		int i = 0;
		
		for(File f : fileArray){
			//System.out.println(f);
			String name = f.getName();
			
			String[] str = s[i].split("\\.");
			
			fileWriter.write("INSERT INTO p_wx_template VALUES(\""+str[0]+"\",\""+str[1]+"\",\"http://move.ehai.xyz/wx_pic/"+name+"\");\r\n");//写入 \r\n换行
			
			i++;
			//System.out.println(newName);
			
		}
		fileWriter.flush();
		fileWriter.close();
 		
 		
 		
 		/*List<String> list = new ArrayList<>();
 		
 		FileWriter fileWriter = new FileWriter("F:/b.txt");
 		
 		String file = txt2String(new File("F:/a.txt"));
 		
 		String[] s = file.split("\r\n");
 		
 		for(int i=0;i<s.length;i++) {
 			
 			
 			
 			String str = s[i].split("\\.")[1];
 			
 			list.add(str);
 			
 		}
 		
 		for(int q=0;q<list.size();q++) {
 			fileWriter.write("INSERT INTO p_wx_template VALUES(\"\",\""+list.get(q)+"\");\r\n");//写入 \r\n换行
 			
 		}
 		
 		
		fileWriter.flush();
		fileWriter.close();*/
 		
	}
 	
}
