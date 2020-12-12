package com.pgy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xch.dto.S_Order;



public class SOrderDao extends BaseDao{
												  	
	private static final String NAMESPACE_NAME = "com.xch.mapper.SOrderMapper.";
	
	
	
	public static void main(String[] args) {
		
		SOrderDao oDao = new SOrderDao();
		
		S_Order o = new S_Order();
		/*o.setMoney(0.04);*/
		o.setOrderId("11");
		/*o.setOrderStatus(5);*/
		
		
		
		o.setStaffId("66");
		
		oDao.updateSOrder(o);
		
		
	}
	
	public List<S_Order> getSOrderList(S_Order order){
		
		SqlSession session = getSqlSession();
		List<S_Order> orderList = session.selectList(NAMESPACE_NAME+"getSOrderList",order);
		closeSqlSession();
		return orderList;
		
	}
	
	public S_Order getSOrder(String orderId){
		//创建数据库用户
		SqlSession session = getSqlSession();
		S_Order order = session.selectOne(NAMESPACE_NAME+"getSOrder",orderId);
		closeSqlSession();
		return order;
	}
	
	public int addOrder(S_Order order){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addSOrder", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public int updateSOrder(S_Order order){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.update(NAMESPACE_NAME+"updateSOrder", order);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	
}
