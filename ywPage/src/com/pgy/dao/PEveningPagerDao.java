package com.pgy.dao;

import org.apache.ibatis.session.SqlSession;

import com.pgy.dto.P_EveningPager;

public class PEveningPagerDao extends BaseDao {
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.PEveningPagerMapper.";
	
	public P_EveningPager getEveningPaperByPagerId(int pagerId){
		
		SqlSession session = getSqlSession();
		
		session.update(NAMESPACE_NAME+"setUTF");
		
		P_EveningPager e = session.selectOne(NAMESPACE_NAME+"getEveningPaperByPagerId",pagerId);
		
		closeSqlSession();
		
		return e;
	}
	
	public int getEveningpaperId(){
		
		SqlSession session = getSqlSession();
		
		session.update(NAMESPACE_NAME+"setUTF");
		
		P_EveningPager e = session.selectOne(NAMESPACE_NAME+"getEveningpaper");
		
		closeSqlSession();
		
		return e.getPagerId();
	}
	
	
}
