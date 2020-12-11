package com.pgy.controller.admin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pgy.common.PageModel;
import com.pgy.common.Util;
import com.pgy.controller.customer.PageController;
import com.pgy.dao.OrderDao;
import com.pgy.dao.UserDao;
import com.pgy.service.MainService;
import com.pgy.service.PageService;
import com.xch.dto.Admin;
import com.xch.dto.Goods;
import com.xch.dto.Order;
import com.xch.dto.Site;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * 后台管理控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/admin")
public class AdminController {
	
	private static Logger logger = LogManager.getLogger(PageController.class);
	
	/**
	 * 获取文章类型接口
	 */
	public static String articleTypeUrl = "https://api1.sppxw.com/ywapp/Article/?CID=";
	/**
	 * 获取用户文章列表接口
	 */
	private static String ArtListUrl = "https://api1.sppxw.com/ywapp/Article/Cli/";
	
	/**
	 * 文章详情接口
	 */
	private static String articleUrl = "https://api1.sppxw.com/art/Articles/";
	
	/**
	 * 新增文章及更新文章接口
	 */
	private static String aArticleUrl = "https://api1.sppxw.com/art/Articles/a/";
	
	/**
	 * 删除文章接口
	 */
	private static String dArticleUrl = "https://api1.sppxw.com/art/Articles/d/";
	
	private static String isCheckUrl = "https://api1.sppxw.com/art/Articles/IsCheck/";
	
	/**
	 * 图片上传接口
	 */
	private static String imgUrl = "https://api2.sppxw.com/ImgDo/";
	
	
	
	PageModel pageModel = new PageModel();
	
	MainService mainService = null;
	
	
	@RequestMapping(value="/toLogin")
	public String toLogin() {
		
		return "logins";
	}
	
	@RequestMapping(value="/toArticle")
	public String toArticle(HttpSession session,int ArtID,Model data) {
		
		
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		
		
		JSONArray CIDArray = JSONArray.fromObject(Util.doGet(articleTypeUrl+"-1").get("data"));
		
		data.addAttribute("CIDArray", CIDArray);
		
		if(ArtID != 0) {
			String url = articleUrl+"?WxOpenID="+WxOpenID+"&ArtID="+ArtID;
			JSONArray artArray = JSONArray.fromObject(Util.doGet(url).get("data"));
			JSONObject artObject = JSONObject.fromObject(artArray.get(0));
			String Title = artObject.getString("Title");
			
			if(Title.contains("\"")) {
				artObject.put("Title", Title.replace("\"", "“"));
			}
			
			data.addAttribute("artObject", artObject);
		}
		
		
		
		return "articles/article";
		
	}
	
	@RequestMapping(value="/toArticleList")
	public String toArticleList(HttpSession session,Model data) {
		
		JSONObject jsonObject = null;
		List<Object> artList = new ArrayList<Object>();
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		/**
		 * 获取文章列表
		 */
		String url = ArtListUrl+"?WxOpenID="+WxOpenID;
		
		JSONArray artArray = JSONArray.fromObject(Util.doGet(url).get("data"));
		
		for(int i=0;i < artArray.size() ;i++) {
			
			jsonObject = artArray.getJSONObject(i);
			artList.add(JSONObject.toBean(jsonObject));
			
		}
		data.addAttribute("artList", artList);
		
		return "articles/articleList";
		
	}
		
	
	@ResponseBody	
	@RequestMapping(value="/addArticle")
	public String addArticle(HttpSession session,int ArtID,String Title,String PhotoCover,String Content,int CID,int FontType,int IsRight) throws IOException {
		
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		
		//文章json
		JSONObject artObject = new JSONObject();
		//拼成Content字段的json数组几json
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		String[] imgUrl;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8"))); 
		String line;
		int jsonType = 0;
		while ( (line = br.readLine()) != null ) { 
			if(!line.trim().equals("")){
				
				
				if(line.contains("img")) {
					if(jsonType == 1) {
						json.put("text", "");
						jsonArray.add(json);
						json = new JSONObject();
					}
					imgUrl = line.split("\"");
					
					json.put("photo", imgUrl[1]);
					
					jsonType = 1;
				}else {
					if(jsonType == 2) {
						json.put("photo", "");
						jsonArray.add(json);
						json = new JSONObject();
					}
					line = line.replace("<p>", "");
					line = line.replace("</p>", "");
					if(!line.equals(" ")) {
						json.put("text", line);
						jsonType = 2;
					}else {
						jsonType = 0;
					}
				}
				
				if(json.has("photo") && json.has("text")) {
					
					jsonArray.add(json);
					json = new JSONObject();
					json.put("photo", "");
					json.put("text", "");
					jsonType = 0;
					
				}
				
				
			} 
		} 
		
		if(!json.has("photo")) {
			json.put("photo", "");
			jsonArray.add(json);
		}else if(!json.has("text")) {
			json.put("text", "");
			jsonArray.add(json);
		}
		
		artObject.put("WxOpenID", WxOpenID);
		artObject.put("ArtID", ArtID);
		artObject.put("Title", Title);
		artObject.put("Content", jsonArray.toString());
		artObject.put("PhotoCover", PhotoCover);
		artObject.put("CID", CID);
		artObject.put("FontType", FontType);
		artObject.put("IsRight", IsRight);
		
		String result = updateAndAddArticle(artObject);
		
		
        
		return "success".equals(result)?"1":result;
	}
	
	@ResponseBody
	@RequestMapping(value="/delArticle")
	public String delArticle(HttpSession session,int ArtID) {
		
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		
		JSONObject json = new JSONObject();
		
		StringBuffer content = new StringBuffer();
		content.append("WxOpenID="+WxOpenID);
		content.append("&ArtID="+ArtID);
        
        String result = Util.sendPost(dArticleUrl, content.toString());
		
        json = JSONObject.fromObject(result);
		
        
        
		return "success".equals(json.getString("msg"))?"1":"0";
	}
	
	public String updateAndAddArticle(JSONObject json) {
		
		StringBuffer content = new StringBuffer();
		content.append("WxOpenID="+json.getString("WxOpenID"));
		content.append("&ArtID="+json.getString("ArtID"));
		content.append("&Title="+json.getString("Title"));
		content.append("&PhotoCover="+json.getString("PhotoCover"));
		content.append("&Content="+json.getString("Content"));
		content.append("&CID="+json.getString("CID"));
		content.append("&IsRight="+json.getString("IsRight"));
        content.append("&FontType="+json.getString("FontType"));
        logger.info("发送文章数据-->"+content.toString());
        String result = Util.sendPost(aArticleUrl, content.toString());
        
        
        
        logger.info("新增或修改文章返回结果-->"+result);
		
        JSONObject resultJson = JSONObject.fromObject(result);
		//审核接口，因为是自己使用的，应该不需要审核
        /*if(resultJson.getInt("code") == 0) {
        	String ArtID = JSONObject.fromObject(resultJson.getString("data")).getString("ArtID");
            
            JSONObject isCheckResult = Util.doGet(isCheckUrl+"?ArtID="+ArtID);
            
            logger.info("调用审核接口返回结果-->"+isCheckResult);
        }*/
		return resultJson.getString("msg");
	}
	
	@ResponseBody
	@RequestMapping(value="/updateIsRight")
	public String updateIsRight(HttpSession session,int ArtID,int IsRight) {
		
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		
		
		String url = articleUrl+"?WxOpenID="+WxOpenID+"&ArtID="+ArtID;
		JSONArray artArray = JSONArray.fromObject(Util.doGet(url).get("data"));
		JSONObject artObject = JSONObject.fromObject(artArray.get(0));
		artObject.put("WxOpenID", WxOpenID);
		artObject.put("IsRight", IsRight);
		String result = updateAndAddArticle(artObject);
		
        
        
		
		return result.equals("success")?"1":result;
	}
	
	/**
	 上传图片 
	* @Title: uploadImage 
	* @Description: 上传图片 
	* @param image 要上传的图片
	* @param request
	* @return Map<String, Object> location(图片要上传的位置)
	* @throws
	 */
	@ResponseBody
	@RequestMapping("/uploadImage")
	public Map<String, Object> uploadImage( @RequestParam("file") MultipartFile file,
			HttpSession session) throws Exception {
		Map<String, Object> ret = new HashMap<>();
		
		Admin admin = (Admin) session.getAttribute(Util.SESSION_ADMIN);
		String WxOpenID = admin.getOpenId();
		
		/*File file1 = MultipartFileToFile.multipartFileToFile(file);
		
		
		String result = Util.postFrom("https://api2.sppxw.com/ImgDo/", WxOpenID,file1);
        
		JSONObject json = JSONObject.fromObject(result);
		if("success".equals(json.getString("msg"))) {
			JSONObject jsonData = JSONObject.fromObject(json.getString("data"));
			ret.put("location", jsonData.getString("photo"));
		} */
		 
		BASE64Encoder encoder = new BASE64Encoder();
        String imgData = encoder.encode(file.getBytes()).replace("+", "%2B");
        
        StringBuffer content = new StringBuffer();
		content.append("WxOpenID="+WxOpenID);
		content.append("&imgInfo="+imgData);
		
		String result = Util.sendPost(imgUrl, content.toString());
		JSONObject json = JSONObject.fromObject(result);
		if("success".equals(json.getString("msg"))) {
			JSONObject jsonData = JSONObject.fromObject(json.getString("data"));
			ret.put("location", jsonData.getString("photo"));
		}
		
		return ret;
	}
	
	
	
	/**
	 * 查询所有订单操作
	 * @param user
	 * @param data
	 * @return
	 */
	@RequestMapping("/orderList")         //映射当前页的参数    并给一个默认值
	public String getOrderList(@RequestParam(name="orderId",defaultValue="")String orderId,
			@RequestParam(name="goodsId",defaultValue="")String goodsId,
			@RequestParam(name="status",defaultValue="")String status,
			@RequestParam(name="pageIndex",defaultValue="0")int pageIndex,Model data){
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		
		
		PageService pageService = new PageService();
		
		List<Goods> goodsList = pageService.getGoodsList();
		
		data.addAttribute("goodsList", goodsList);
		
		//将传进来的参数设置到params
		params.put("orderId", orderId);
		params.put("goodsId", goodsId);
		params.put("status", status);
		OrderDao orderDao = new OrderDao();
		//将获取到模糊查询的pic给到count方法，如果有模糊查询，则总数量只为模糊查询后的数量
		int recordCount = orderDao.count(params);
		
		//设置从Dao方法中获取的统计商品数量给到pageModel
		pageModel.setRecordCount(recordCount);
		//设置当前页
		pageModel.setPageIndex(pageIndex);
		//将分页工具类设置到params   以上下个参数用于传到getPageGoods中用于真正的数据操作
		
		if(recordCount != 0){
			
			params.put("pageModel", pageModel);
			
			List<Order> orderList = pageService.getOrderListPage(params);
			
			data.addAttribute("orderList", orderList);
		}
		
		
		
		data.addAttribute("pageModel", pageModel);	 
		data.addAttribute("orderId", orderId);
		data.addAttribute("goodsId", goodsId);
		data.addAttribute("status", status);
		return "order/orderList";
	}
	
	
	/**
	 * 管理员ajax登录
	 * @param userId
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/adminLoginAjax")
	public Map<String, Object> login(String userId,String password,String vcode,HttpSession session){
		logger.info("--login--"+userId+"----"+password+"----"+vcode);
		Map<String, Object> map = new HashMap<>();
		UserDao userDao = null;
		//从Session中获取验证码
		String oldVcode = (String)session.getAttribute(AuthCodeAction.AUTH_CODE);
		
		if(vcode == null || ! oldVcode.equals(vcode)){
			map.put("tip", "验证码不正确");
			map.put("status", 1);
		}else{
			
			userDao = new UserDao();
			Admin admin = userDao.getAdminByAdminName(userId);
			
			if(admin != null && password.equals(admin.getPassword())) {
				session.setAttribute(Util.SESSION_ADMIN, admin);
				map.put("tip", "管理员登录成功");
				map.put("status", 0);
			}else {
				map.put("tip", "管理员名或密码不正确！");
				map.put("status", 3);
			}
			
		}
		
		return map;
	}
	
	/**
	 * 跳转管理员后台方法
	 * @return
	 */
	@RequestMapping(value="/main")
	public String main(){
		
		
		return "main";
	}
	
	@ResponseBody
	@RequestMapping(value="getSiteBySiteId")
	public Site getSiteBySiteId(int siteId) {
		
		mainService = new MainService();
		
		Site site = mainService.getSiteBySiteId(siteId);
		
		return site;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/updateOrderStatusByOrderId")
	public String updateOrderStatusByOrderId(String orderId) {
		
		mainService = new MainService();
		
		String code = mainService.updateOrderStatusByOrderId(orderId);
		
		return code;
	}
	
	/**
	 * 管理员注销处理方法
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logou(HttpSession session){
		//用户名注销(注销)
		session.invalidate();
		
		return "logins";
	}
	
	
}
