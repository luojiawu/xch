package com.pgy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pgy.common.Util;
import com.xch.dto.S_Order;



public class OrderDao extends BaseDao{
												  	
	private static final String NAMESPACE_NAME = "com.xch.mapper.SOrderMapper.";
	
	public int count(Map<String,Object> params){
		//创建数据库用户
		SqlSession session = getSqlSession();
		int count = session.selectOne(NAMESPACE_NAME+"count",params);
		closeSqlSession();
		return count;
	}
	
	public static void main(String[] args) {
		
		OrderDao oDao = new OrderDao();
		
		S_Order o = new S_Order();
		
		o.setOrderId("11");
		o.setServerId(1);
		o.setBooking("11");
		o.setOrderStatus(1);
		o.setName("11");
		o.setPhone("1");
		oDao.addOrder(o);
		
		
	}
	
	public List<S_Order> getPageOrder(){
		//创建数据库用户
		SqlSession session = getSqlSession();
		List<S_Order> OrderList = session.selectList(NAMESPACE_NAME+"getMTemplateBookList");
		closeSqlSession();
		return OrderList;
	}
	
	public int addOrder(S_Order order){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addSOrder", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	
	/*public List<Order> getPageOrder(Map<String,Object> params){
		//创建数据库用户
		SqlSession session = getSqlSession();
		List<Order> OrderList = session.selectList(NAMESPACE_NAME+"findByPage",params);
		closeSqlSession();
		return OrderList;
	}
	
	public static Order timeContrast(Order order,SqlSession sqlSession) {
		
		long l = Long.valueOf(order.getOrderTime()); 
		
		long closeTime = l+Util.PAYCLOSETIME*60*60*1000;
		//如果
		if(System.currentTimeMillis() > closeTime) {
			
			order.setStatus(-1);
			
			int i = sqlSession.update(NAMESPACE_NAME+"updateOrderStatus", order);
			
			sqlSession.commit();
			
		}
		
		return order;
	} 
	
	
	
	public int addOrder(Order order){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addOrder", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public Order getOrderByOrderId(String orderId) {
		
		SqlSession sqlSession = getSqlSession();
		
		Order order = sqlSession.selectOne(NAMESPACE_NAME+"getOrderByOrderId", orderId);
		
		if(0 == order.getStatus()) {
			timeContrast(order, sqlSession);
		}		
		
		closeSqlSession();
		
		return order;
		
	}
	
	
	public List<Order> getOrderListByOpenId(String openId) {
		
		SqlSession sqlSession = getSqlSession();
		
		List<Order> orderList = sqlSession.selectList(NAMESPACE_NAME+"getOrderByOpenId", openId);
		
		for(int i=0;i<orderList.size();i++) {
			Order order = orderList.get(i);
			
			if(0 == order.getStatus()) {
				timeContrast(order,sqlSession);
			} 
			
		}
		
		closeSqlSession();
		
		return orderList;
		
	}
	
	public int updateOrderStatusByOrderId(String orderId,int status) {
		
		SqlSession sqlSession = getSqlSession();
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("orderId", orderId);
		map.put("status", status);
		
		int i = sqlSession.update(NAMESPACE_NAME+"updateOrderStatusByOrderId", map);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
	public int updateOrderStatus(String orderId,String wxOrderId,double payMoney,int goodsNum,String payTime) {
		
		Order order = new Order();
		order.setOrderId(orderId);
		order.setWxOrderId(wxOrderId);
		order.setPayMoney(payMoney);
		order.setGoodsNum(goodsNum);
		order.setPayTime(payTime);
		order.setStatus(1);
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.update(NAMESPACE_NAME+"updateOrderStatus", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
	public int delOrderByOrderId(String orderId) {
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.delete(NAMESPACE_NAME+"delOrderByOrderId", orderId);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
	public int updateOrderSiteId(String orderId,int siteId) {
		
		Order order = new Order();
		
		order.setOrderId(orderId);
		order.setSiteId(siteId);
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.update(NAMESPACE_NAME+"updateOrderSiteId", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}*/
	
}
