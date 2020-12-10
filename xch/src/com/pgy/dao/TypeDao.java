package com.pgy.dao;

import org.apache.ibatis.session.SqlSession;

import com.pgy.dto.Type;



public class TypeDao extends BaseDao{
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.TypeMapper.";
	
	
	public Type getTypeByIds(int typeId) {
		
		SqlSession sqlSession = getSqlSession();
		
		Type type = sqlSession.selectOne(NAMESPACE_NAME+"getTypeById",typeId);

		closeSqlSession();
		
		return type;
		
	}
	
}
