package com.pgy.service;

import java.util.List;
import java.util.Map;

import com.pgy.common.Util;
import com.pgy.dao.GoodsDao;
import com.pgy.dao.OrderDao;
import com.pgy.dao.PhotoDao;
import com.pgy.dao.TypeDao;
import com.xch.dto.Goods;
import com.xch.dto.Order;
import com.xch.dto.Type;

public class PageService {
	
	GoodsDao goodsDao = new GoodsDao();
	
	OrderDao orderDao = new OrderDao();
	
	TypeDao typeDao = new TypeDao();
	
	PhotoDao photoDao = new PhotoDao();
	
	/**
	 * 获取有分页信息的订单列表
	 * @param openId
	 * @return
	 */
	public List<Order> getOrderListPage(Map<String,Object> params){
		
		List<Order> orderList = orderDao.getPageOrder(params);
		
		for(int i=0;i<orderList.size();i++) {
			
			Goods goods = goodsDao.getGoodsById(orderList.get(i).getGoodsId());
			orderList.get(i).setGoods(goods);
			
			Type type = typeDao.getTypeByIds(orderList.get(i).getTypeId());
			orderList.get(i).setType(type);
		}
		
		return orderList;
		
	}
	
	/**
	 * 获取订单列表
	 * @param openId
	 * @return
	 */
	public List<Order> getOrderListByOpenId(String openId){
		
		
		
		List<Order> orderList = orderDao.getOrderListByOpenId(openId);
		
		for(int i=0;i<orderList.size();i++) {
			
			orderList.get(i).setOrderTime(Util.timeSwitch(orderList.get(i).getOrderTime()));
			
			String firstPhoto = photoDao.getFirstPhoto(orderList.get(i).getOrderId());
			orderList.get(i).setFirstPhoto(firstPhoto);
			
			Goods goods = goodsDao.getGoodsById(orderList.get(i).getGoodsId());
			orderList.get(i).setGoods(goods);
			
			Type type = typeDao.getTypeByIds(orderList.get(i).getTypeId());
			orderList.get(i).setType(type);
		}
		
		//根据订单信息获取详细的订单信息。
		
		return orderList;
		
	}
	
	/**
	 * 获取商品列表
	 * @return
	 */
	public List<Goods> getGoodsList(){
		
		List<Goods> goodsList = goodsDao.getGoodsList();
		
		return goodsList;
		
	}
	
	/**
	 * 根据商品id获取商品信息
	 * @return
	 */
	public Goods getGoodsById(String goodsId) {
		
		Goods goods = goodsDao.getGoodsById(Integer.valueOf(goodsId));
		
		String goodsType = goods.getGoodsType();
		String[] typeIds = goodsType.split(",");
		
		List<Type> typeList = goodsDao.getTypeByIds(typeIds);
		
		for(int i = 0;i < typeList.size();i++) {
			String s = typeList.get(i).getMainImg();
			
			String mainImgs[] = s.split(",");
			
			typeList.get(i).setMainImgs(mainImgs);
			
		}
		
		
		goods.setTypeList(typeList);
		
		return goods;
		
	}
	
	
}
