package com.pgy.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;



public class PhotoDao extends BaseDao{
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.PhotoMapper.";
	
	public int addPhoto(List<String> photoUrl,String orderId){
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("photoUrl", photoUrl);
		map.put("orderId", orderId);
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addPhoto", map);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public String getFirstPhoto(String orderId) {
		
		SqlSession sqlSession = getSqlSession();
		
		String s = sqlSession.selectOne(NAMESPACE_NAME+"getFirstPhoto", orderId);
		
		closeSqlSession();
		
		return s;
		
	}
	
	public int delPhotoByOrderId(String orderId) {
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.delete(NAMESPACE_NAME+"delPhotoByOrderId", orderId);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
}
