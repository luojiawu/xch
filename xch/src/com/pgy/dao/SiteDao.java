package com.pgy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xch.dto.Site;



public class SiteDao extends BaseDao{
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.SiteMapper.";
	
	public int addAndReplaceSite(Site site){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addAndReplaceSite", site);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public int delSiteBySiteId(int siteId) {

		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.delete(NAMESPACE_NAME+"delSiteBySiteId", siteId);
				
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
	public Site getSiteBySiteId(int siteId) {
		
		SqlSession sqlSession = getSqlSession();
		
		Site site = sqlSession.selectOne(NAMESPACE_NAME+"getSiteBySiteId", siteId);
				
		closeSqlSession();
		
		return site;
		
	}
	
	public List<Site> getSiteBySiteIds(String[] siteIds) {
		
		SqlSession sqlSession = getSqlSession();
		
		List<Site> siteList = sqlSession.selectList(NAMESPACE_NAME+"getSiteBySiteIds", siteIds);
				
		closeSqlSession();
		
		return siteList;
		
	}
	
	
	
}
