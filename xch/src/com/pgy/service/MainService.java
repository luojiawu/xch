package com.pgy.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.pgy.common.Util;
import com.pgy.dao.GoodsDao;
import com.pgy.dao.OrderDao;
import com.pgy.dao.PhotoDao;
import com.pgy.dao.SiteDao;
import com.pgy.dao.TypeDao;
import com.pgy.dao.UserDao;
import com.xch.dto.Goods;
import com.xch.dto.Order;
import com.xch.dto.Site;
import com.xch.dto.Type;
import com.xch.dto.User;

public class MainService {
	
	private static Logger logger = LogManager.getLogger(MainService.class);
	
	PhotoDao photoDao = new PhotoDao();

	OrderDao orderDao = new OrderDao();
	
	GoodsDao goodsDao = new GoodsDao();
	
	TypeDao typeDao = new TypeDao();
	
	UserDao userDao = new UserDao();
	
	SiteDao siteDao = new SiteDao();
	
	
	public Site getSiteBySiteId(int siteId) {
		
		Site site = siteDao.getSiteBySiteId(siteId);
		
		return site;
		
	}
	
	public String pickOrderSiteId(String orderId,int siteId) {
		
		int i = orderDao.updateOrderSiteId(orderId, siteId);
		return i>0?"1":"0";
	}
	
	public String delSiteBySiteId(int siteId,String openId) {
		//删除地址，删除用户中的地址id
		
		User user = userDao.getUserByOpenId(openId);
		
		String[] siteIds = user.getSiteId().split(",");
		
		for(int j=0;j<siteIds.length;j++) {
			
			if(siteId == Integer.valueOf(siteIds[j])) {
				String sId = user.getSiteId().replace(siteIds[j]+",", "");
				user.setSiteId(sId);
			}
		}
		int i = userDao.addAndReplaceUser(user);
		
		if(i > 0) {
			i = siteDao.delSiteBySiteId(siteId);
		}
		
		
		return i>0?"1":"0";
	}
	
	public String addAndReplaceSite(String openId,Site site,int defaultStatus,String orderId,int checkedSiteId) {
		
		int siteId = site.getSiteId();
		int i = siteDao.addAndReplaceSite(site);
		String result = "";
		if(i>=1) {
			
			User user = userDao.getUserByOpenId(openId);
			if(null == user.getSiteId()) {
				user.setSiteId("");
			}
			String[] siteIds = user.getSiteId().split(",");
			
			//如果不等于0则不是新增地址，否则按新增地址处理
			if(0 != siteId) {
				for(int j=0;j<siteIds.length;j++) {
					
					if(site.getSiteId() == Integer.valueOf(siteIds[j])) {
						//如果已存在的地址需要设置为默认地址，则先删除，再将地址加到字符串前面
						if(1 == defaultStatus){
							String sId = user.getSiteId().replace(siteIds[j]+",", "");
							user.setSiteId(siteIds[j]+","+sId);
						}else if(j==0 && 0 ==  defaultStatus){
							String sId = user.getSiteId().replace(siteIds[j]+",", "");
							user.setSiteId(sId+siteIds[j]+",");
						}
						
						break;
					}
					
				}
			}else {
				//如果等于1表示设为默认地址，则添加到字符串前面
				if(1 == defaultStatus) {
					user.setSiteId(site.getSiteId()+","+user.getSiteId()); 
				}else {
					user.setSiteId(user.getSiteId()+site.getSiteId()+","); 
				}
				
			}
			result = userDao.addAndReplaceUser(user)>0?"1":"0";
			
			//checkedSiteId等于0说明该用户第一次填地址信息，可以直接把地址给到订单数据中
			if(checkedSiteId == 0) {
				
				result = orderDao.updateOrderSiteId(orderId, site.getSiteId())>0?"1":"0";
				
			}
			
			
		}
		
		return result;
		
	}
	
	public String updateOrderStatusByOrderId(String orderId) {
		
		int i = orderDao.updateOrderStatusByOrderId(orderId, 2);
		
		return i>0?"1":"0";
	}
	
	public String updateOrderStatus(String orderId,String wxOrderId,double payMoney,int goodsNum,String payTime) {
		
		int i = orderDao.updateOrderStatus(orderId, wxOrderId, payMoney, goodsNum, payTime);
		
		
		return i>0?"1":"0";
	}
	
	public void addAndReplaceUser(String openId,String unionId) {
		
		User user = new User();
		
		user.setOpenId(openId);
		user.setUnionId(unionId);
		user.setLastTime(String.valueOf(System.currentTimeMillis()));
		
		userDao.addAndReplaceUser(user);
		
	}
	
	public HashMap<String, Object> getMoneyByOrderId(String orderId) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		Order order = orderDao.getOrderByOrderId(orderId);
		map.put("money", order.getMoney());
		Goods goods = goodsDao.getGoodsById(order.getGoodsId());
		map.put("postage", goods.getPostage());
		
		return map;
	}
	
	
	
	
	public List<Site> getSiteList(String openId){
		
		User user = userDao.getUserByOpenId(openId);
		
		String[] siteIds = user.getSiteId().split(",");
		
		List<Site> siteList = siteDao.getSiteBySiteIds(siteIds);
		
		return siteList;
		
	}
	
	//获取订单详情和默认地址
	public HashMap<String, Object> getOrderAndSite(String orderId) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		Order order = orderDao.getOrderByOrderId(orderId);
		
		order.setFirstPhoto(photoDao.getFirstPhoto(order.getOrderId()));
		
		map.put("order", order);
		
		Goods goods = goodsDao.getGoodsById(order.getGoodsId());
		map.put("goods", goods);
		Type type = typeDao.getTypeByIds(order.getTypeId());
		map.put("type", type);
		//如果订单中的地址id不等于0则表示有
		if(0 != order.getSiteId()) {
			//获取地址详细信息
			Site site = siteDao.getSiteBySiteId(order.getSiteId());
			map.put("site", site);
		}
		
		return map;
		
	}
	
	public static void main(String[] args) {
		//boolean bool = Util.delFile(new File("F:/javaWeb/photo/gB1m11657524"));
		
		String orderId = "gB1m11657524";
		
		MainService adminService = new MainService();
		
		adminService.delOrderByOrderId(orderId);
	}
	
	
	public int delOrderByOrderId(String orderId) {
		int i = orderDao.delOrderByOrderId(orderId);
		if(i >= 1) {
			
			i = photoDao.delPhotoByOrderId(orderId);
			if(i >= 1) {
				logger.info("删除订单号"+Util.SAVECONTENTS+"/"+orderId+"的照片");
				boolean bool = Util.delFile(new File(Util.SAVECONTENTS+"/"+orderId));
				logger.info("删除结果："+String.valueOf(bool));
			}
		}
		
		return i;
	}
	
	/**
	 * 生成订单
	 * @return
	 */
	public int placeOrder(List<String> photoUrl,String orderId,String openId,int goodsId,int typeId) {
		
		int i = photoDao.addPhoto(photoUrl, orderId);
		
		Order order = new Order();
		
		order.setOrderId(orderId);
		order.setOpenId(openId);
		order.setGoodsId(goodsId);
		order.setTypeId(typeId);
		
		int photoNum = photoUrl.size();
		order.setPhotoNum(photoNum);
		
		Type type = typeDao.getTypeByIds(typeId);
		
		int defaultPaper = type.getDefaultPaper();
		//如果实际纸张大于默认纸张
		if(photoNum>defaultPaper) {
			//多出的纸张
			int num = photoNum-defaultPaper;
			//多出的纸张乘以单价
			double price = Util.multiply(num,type.getUnitPrice());
							//默认价格加上多出的金额
			order.setMoney(Util.add(type.getDefaultPrice(), price));
		}else {
			
			order.setMoney(type.getDefaultPrice());
			
		}
		
		User user = userDao.getUserByOpenId(openId);
		if( !"".equals(user.getSiteId()) && null != user.getSiteId()) {
			String[] siteIds = user.getSiteId().split(",");
			//如果用户有地址，则把地址录入到订单里
			
			order.setSiteId(Integer.valueOf(siteIds[0]));
			
		}
		
		
		order.setOrderTime(String.valueOf(System.currentTimeMillis()));
		//微信支付完成后的参数，先给默认的数据，等支付完成回调的时候再更新
		order.setStatus(0);
		order.setWxOrderId("");
		order.setGoodsNum(1);
		order.setPayMoney(0);
		order.setPayTime("");
		
		
		i = orderDao.addOrder(order);
		
		
		
		return i;
	}
	
	
}
