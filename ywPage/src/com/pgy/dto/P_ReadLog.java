package com.pgy.dto;

public class P_ReadLog {
	
	private int readId;
	
	private int anaId;
	
	private String openId;
	
	private String nickname;
	
	private String headimgurl;
	
	private String readTime;
	
	private int count;
	
	private P_User user;
	
	public int getReadId() {
		return readId;
	}

	public void setReadId(int readId) {
		this.readId = readId;
	}

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
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
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
