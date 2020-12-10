package com.pgy.dto;

public class P_ReadLog {
	
	private int anaId;
	
	private String openId;
	
	private String readTime;
	
	private int count;
	
	private P_User user;

	public int getAnaId() {
		return anaId;
	}

	public void setAnaId(int anaId) {
		this.anaId = anaId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public P_User getUser() {
		return user;
	}

	public void setUser(P_User user) {
		this.user = user;
	}
	
}
