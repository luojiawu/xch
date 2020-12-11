package com.pgy.service;

import java.util.List;

import com.pgy.dao.MOrderDao;
import com.pgy.dao.MTemplateBookDao;
import com.xch.dto.M_Order;
import com.xch.dto.M_TemplateBook;

public class MoveService {
	
	MOrderDao mOrderDao = null;
	
	MTemplateBookDao mTemplateBookDao = null;
	
	public M_Order getMOrder(String orderId) {
		
		mOrderDao = new MOrderDao();
		
		M_Order m_Order = mOrderDao.getMOrder(orderId);
		
		//如果数据库查不到订单数据，则调用接口获取订单数据
		if(m_Order == null) {
			m_Order = getTBOrder(orderId);
			//将获取到的数据录入数据库
			if(m_Order != null) {
				mOrderDao.addMOrder(m_Order);
			}
		}
		
		
		return m_Order;
		
	}
	
	public List<M_TemplateBook> getMTemplateBookList(){
		
		mTemplateBookDao = new MTemplateBookDao();
		
		List<M_TemplateBook> m_TemplateBookList = mTemplateBookDao.getMTemplateBookList();
		
		return m_TemplateBookList;
		
	}
	
	
	/**
	 * 调用接口获取淘宝订单号
	 * @return
	 */
	public static M_Order getTBOrder(String orderId) {
		
		M_Order m_Order = null;
		if(orderId.equals("123456789")) {
			m_Order = new M_Order();
			m_Order.setOrderId(orderId);
			m_Order.setPhotoNum(16);
		}
		
		return m_Order;
		
	}
	
}
