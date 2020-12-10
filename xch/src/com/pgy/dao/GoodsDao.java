package com.pgy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pgy.dto.Goods;
import com.pgy.dto.Type;



public class GoodsDao extends BaseDao{
	
	private static final String NAMESPACE_NAME = "com.pgy.mapper.PageMapper.";
	
	public List<Goods> getGoodsList(){
		
		SqlSession sqlSession = getSqlSession();
		
		List<Goods> goodsList = sqlSession.selectList(NAMESPACE_NAME+"getGoodsList"); 
				
		closeSqlSession();
		
		return goodsList;
	}
	
	public Goods getGoodsById(int goodsId) {
		
		SqlSession sqlSession = getSqlSession();
		
		Goods goods = sqlSession.selectOne(NAMESPACE_NAME+"getGoodsById", goodsId);
		
		closeSqlSession();
		
		return goods;
		
	}
	
	public List<Type> getTypeByIds(String[] typeIds) {
		
		SqlSession sqlSession = getSqlSession();
		
		List<Type> typeList = sqlSession.selectList(NAMESPACE_NAME+"getTypeByIds",typeIds);

		closeSqlSession();
		
		return typeList;
		
	}
	
}
