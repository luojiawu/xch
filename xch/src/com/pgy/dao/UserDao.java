package com.pgy.dao;

import org.apache.ibatis.session.SqlSession;

import com.xch.dto.Admin;
import com.xch.dto.User;



public class UserDao extends BaseDao{
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.UserMapper.";
	
	public int addAndReplaceUser(User user){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addAndReplaceUser",user); 
				
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public User getUserByOpenId(String openId) {
		
		SqlSession sqlSession = getSqlSession();
		
		User user = sqlSession.selectOne(NAMESPACE_NAME+"getUserByOpenId",openId); 
				
		closeSqlSession();
		
		return user;
		
	}
	
	/**
	 * 获取管理员账号信息
	 * @return
	 */
	public Admin getAdminByAdminName(String adminName) {
		
		SqlSession sqlSession = getSqlSession();
		
		Admin admin = sqlSession.selectOne(NAMESPACE_NAME+"getAdminByAdminName",adminName); 
				
		closeSqlSession();
		
		return admin;
		
	}
	
}
