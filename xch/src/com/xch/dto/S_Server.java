package com.xch.dto;

/**
 * 服务传输类
 * @author Administrator
 *
 */
public class S_Server {
	
	private int serverId;
	
	/**
	 * 服务名
	 */
	private String serverName;
	
	
	/**
	 * 服务图标
	 */
	private String serverIconUrl;


	public int getServerId() {
		return serverId;
	}


	public void setServerId(int serverId) {
		this.serverId = serverId;
	}


	public String getServerName() {
		return serverName;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public String getServerIconUrl() {
		return serverIconUrl;
	}


	public void setServerIconUrl(String serverIconUrl) {
		this.serverIconUrl = serverIconUrl;
	}
	
	
	
}
