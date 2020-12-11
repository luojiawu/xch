package com.xch.dto;

/**
 * 用户传输类
 * @author Administrator
 *
 */
public class S_User {
	
	private String openId;
	
	private String nickName;
	
	private String headImgUrl;
	
	private String unionId;

	/**
	 * 注册时间
	 */
	private String regTime;
	
	/**
	 * 权限，1为用户，2为员工，默认1
	 */
	private int power;
	
	/**
	 * 员工头像
	 */
	private String staffImgUrl;
	
	/**
	 * 员工姓名
	 */
	private String staffName;
	
	/**
	 * 员工手机号码
	 */
	private String staffPhone;
	
	/**
	 * 服务范围
	 */
	private String server;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getStaffImgUrl() {
		return staffImgUrl;
	}

	public void setStaffImgUrl(String staffImgUrl) {
		this.staffImgUrl = staffImgUrl;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffPhone() {
		return staffPhone;
	}

	public void setStaffPhone(String staffPhone) {
		this.staffPhone = staffPhone;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
}
