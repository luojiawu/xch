package com.pgy.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pgy.dto.P_Ana;
import com.pgy.dto.P_ReadLog;
import com.pgy.dto.P_Template;
import com.pgy.dto.P_User;

public class PPositiveDao extends BaseDao {
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.PPositiveMapper.";
	
	/**
	 * 添加语录，返回主键id
	 * @param ana
	 * @return
	 */
	public int addAna(P_Ana ana){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addAna", ana);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return ana.getAnaId();
	}
	
	public int addUser(P_User user){
		
		SqlSession sqlSession = getSqlSession();
		
		sqlSession.update(NAMESPACE_NAME+"setUTF");
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addUser", user);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public int addReadLog(P_ReadLog readLog) {
	    
		SqlSession sqlSession = getSqlSession();
	    
		int i = sqlSession.insert(NAMESPACE_NAME+"addReadLog", readLog);
	    
		sqlSession.commit();
	    
		closeSqlSession();
	    
		return readLog.getReadId();
	}
	
	public int updateUserSet(P_User user) {
		
		SqlSession sqlSession = getSqlSession();
		
		sqlSession.update(NAMESPACE_NAME+"setUTF");
		
		int i = sqlSession.update(NAMESPACE_NAME+"updateUserSet", user);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
		
	}
	
	public List<P_ReadLog> getReadLog(String openId) {
	    
		SqlSession sqlSession = getSqlSession();
	    
		List<P_ReadLog> list = sqlSession.selectList(NAMESPACE_NAME+"getReadLog", openId);
	    
		closeSqlSession();
	    
		return list;
    }
	
	public List<P_ReadLog> getReadLogByAnaId(int anaId) {
	    
		SqlSession sqlSession = getSqlSession();
	    
		List<P_ReadLog> list = sqlSession.selectList(NAMESPACE_NAME+"getReadLogByAnaId", Integer.valueOf(anaId));
	    
		closeSqlSession();
	    
		return list;
    }
	
	public int updateReadTimeByReadId(P_ReadLog readLog){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"updateReadTimeByReadId", readLog);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	public int updateColorByAnaId(P_Ana ana){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"updateColorByAnaId", ana);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	
	public int updateUser(P_User user){
		
		SqlSession sqlSession = getSqlSession();
		
		sqlSession.update(NAMESPACE_NAME+"setUTF");
		
		int i = sqlSession.insert(NAMESPACE_NAME+"updateUser", user);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
	
	public P_Ana getAnaIdByAna(P_Ana ana){
		
		SqlSession session = getSqlSession();
		
		P_Ana i = session.selectOne(NAMESPACE_NAME+"getAnaIdByAna",ana);
		
		closeSqlSession();
		
		return i;
	}
	
	public P_User getUserByOpenId(String openId){
		
		SqlSession session = getSqlSession();
		
		session.update(NAMESPACE_NAME+"setUTF");
		
		P_User p_user = session.selectOne(NAMESPACE_NAME+"getUserByOpenId",openId);
		
		closeSqlSession();
		
		return p_user;
	}
	
	public P_Template getTemplateByTemplateId(int TemplateId){
		
		SqlSession session = getSqlSession();
		
		P_Template template = session.selectOne(NAMESPACE_NAME+"getTemplateById",TemplateId);
		
		closeSqlSession();
		
		return template;
	}
	
	public List<P_Ana> getAnaByOpenId(String openId) {
	    
		SqlSession session = getSqlSession();
	    
		List<P_Ana> anaList = session.selectList(NAMESPACE_NAME+"getAnaByOpenId", openId);
	    
		closeSqlSession();
	    
		return anaList;
    }
	
	public List<P_User> getUserByBindOpenId(String openId) {
	    
		SqlSession session = getSqlSession();
	    
		session.update(NAMESPACE_NAME+"setUTF");
		
		List<P_User> userList = session.selectList(NAMESPACE_NAME+"getUserByBindOpenId", openId);
	    
		closeSqlSession();
	    
		return userList;
    }
	
	public List<P_Ana> getAnaByBindOpenId(String openId) {
	    
		SqlSession session = getSqlSession();
	    
		List<P_Ana> anaList = session.selectList(NAMESPACE_NAME+"getAnaByBindOpenId", openId);
	    
		closeSqlSession();
	    
		return anaList;
    }
	
	public P_Ana getAnaByAnaId(int anaId){
		
		SqlSession session = getSqlSession();
		
		P_Ana ana = session.selectOne(NAMESPACE_NAME+"getAnaByAnaId",anaId);
		
		closeSqlSession();
		
		return ana;
	}
	
	public int getTemplateCount() {
		
		SqlSession session = getSqlSession();
		
		int count = session.selectOne(NAMESPACE_NAME+"templateCount");
		
		closeSqlSession();
		
		return count;
	}
	
	public List<P_User> getUserByPushedType(P_User user) {
	    
		SqlSession session = getSqlSession();
	    
		List<P_User> userList = session.selectList(NAMESPACE_NAME+"getUserByPushedType", user);
	    
		closeSqlSession();
	    
		return userList;
    }
	
	
}
