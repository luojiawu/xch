package com.xch.dto;

/**
 * 地址传输类
 * @author Administrator
 *
 */
public class S_Site {

	private String openId;
	
	private String name;
	
	private String phone;
	
	/**
	 * 地址详情
	 */
	private String siteDetails;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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
	
}
