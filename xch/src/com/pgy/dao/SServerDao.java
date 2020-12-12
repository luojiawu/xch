package com.pgy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.xch.dto.S_Server;



public class SServerDao extends BaseDao{
												  	
	private static final String NAMESPACE_NAME = "com.xch.mapper.SServerMapper.";
	
	
	
	
	public List<S_Server> getSServerList(){
		
		SqlSession session = getSqlSession();
		List<S_Server> serverList = session.selectList(NAMESPACE_NAME+"getSServerList");
		closeSqlSession();
		return serverList;
		
	}
	
	public S_Server getSServer(String serverId){
		//创建数据库用户
		SqlSession session = getSqlSession();
		S_Server server = session.selectOne(NAMESPACE_NAME+"getSServer",serverId);
		closeSqlSession();
		return server;
	}
	
	public int addServer(S_Server server){
		
		SqlSession sqlSession = getSqlSession();
		
		int i = sqlSession.insert(NAMESPACE_NAME+"addServer", server);
		
		sqlSession.commit();
		
		closeSqlSession();
		
		return i;
	}
	
}
