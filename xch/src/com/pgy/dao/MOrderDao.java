package com.pgy.dao;

import org.apache.ibatis.session.SqlSession;

import com.xch.dto.M_Order;

public class MOrderDao extends BaseDao{
																 
	private static final String NAMESPACE_NAME = "com.pgy.mapper.MOrderMapper.";
	
	public M_Order getMOrder(String orderId){
		
		SqlSession session = getSqlSession();
		M_Order m_Order = session.selectOne(NAMESPACE_NAME+"getMOrder",orderId);
		closeSqlSession();
		return m_Order;
	
	}
	
	public int addMOrder(M_Order m_Order) {
		
		SqlSession session = getSqlSession();
		int i = session.insert(NAMESPACE_NAME+"addMOrder",m_Order);
		session.commit();
		closeSqlSession();
		return i;
		
	}
	
}
