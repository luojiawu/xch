package com.pgy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pgy.dto.M_TemplateBook;

public class MTemplateBookDao extends BaseDao{
																 
	private static final String NAMESPACE_NAME = "com.pgy.mapper.MTemplateBookMapper.";
	
	public List<M_TemplateBook> getMTemplateBookList(){
		
		SqlSession session = getSqlSession();
		List<M_TemplateBook> m_TemplateBookList  = session.selectList(NAMESPACE_NAME+"getMTemplateBookList");
		closeSqlSession();
		return m_TemplateBookList;
		
		
	
	}
	
	
	
}
