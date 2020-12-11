package com.xch.dto;


/**
 * 订单传输类
 * @author Administrator
 *
 */
public class S_Order {
	
	private String orderId;
	
	/**
	 * 服务
	 */
	private int serverId;
	
	/**
	 * 预约时间
	 */
	private String booking;
	
	/**
	 * 订单状态，1为待报价，2为待支付、3为已完成，默认为1
	 */
	private int orderStatus;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 地址详情
	 */
	private String siteDetails;
	
	/**
	 * 备注信息
	 */
	private String remarksInfo;
	
	/**
	 * 备注图片
	 */
	private String remarksImg;
	
	/**
	 * 下单时间
	 */
	private String startTime;
	
	/**
	 * 订单完成时间
	 */
	private String endTime;
	
	/**
	 * 员工openId
	 */
	private String staffId;
	
	/**
	 * 报价
	 */
	private double money;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getBooking() {
		return booking;
	}

	public void setBooking(String booking) {
		this.booking = booking;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(String siteDetails) {
		this.siteDetails = siteDetails;
	}

	public String getRemarksInfo() {
		return remarksInfo;
	}

	public void setRemarksInfo(String remarksInfo) {
		this.remarksInfo = remarksInfo;
	}

	public String getRemarksImg() {
		return remarksImg;
	}

	public void setRemarksImg(String remarksImg) {
		this.remarksImg = remarksImg;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
}
