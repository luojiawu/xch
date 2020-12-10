package com.pgy.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;





public class Config 
{
	
	private String m_sdkAesKey = null;
	private String m_sdkAesInitParam = null;
	private String m_readDataSourcePath = null;
	private String m_writeDataSourcePath = null;
	private int m_signCheckTime = 1000;
	private String m_talkingDataUrl = "http://api.talkinggame.com/api/charge/"; 
	private int m_gameNotifyThreadsNum = 3;
	private int m_talkingNotifyThreadsNum = 3;
	private int m_httpConnectTimeOut = 5000;
	private int m_httpConnectRequestTimeOut = 5000;
	private int m_httpSocketTimeOut = 10000;
	private boolean m_doRequestStatistics = false;
	private int m_statisticsQuerySize = 10000;
	private String m_loggerServerUrl = "";
	private String m_loggerServerUrlNew = "";
	private boolean m_enableDoLogger = false;
	private int m_cacheMapSize = 10000;
	private int m_maxCacheNum = 100000;
	private HashMap<String,Boolean> m_mapChannelLoginCheck = new HashMap<String,Boolean>();
	private HashMap<Integer,Integer> m_mapGameAndChannelLoginCheck = new HashMap<Integer,Integer>();
	private int m_appstoreOrderDuringTime = 60*60*8;
	private int m_appstoreOrderMaxNum = 10;
	public int getAppstoreOrderMaxNum()
	{
		return m_appstoreOrderMaxNum;
	}
	public int getAppstoreOrderDuringTime()
	{
		return m_appstoreOrderDuringTime;
	}
	public int getMaxCacheNum()
	{
		return m_maxCacheNum;
	}
	
	public boolean getEnableDoLogger()
	{
		return m_enableDoLogger;
	}
	public String getLoggerServerUrl()
	{
		return m_loggerServerUrl;
	}
	public String getLoggerServerNew()
	{
		return m_loggerServerUrlNew;
	}
	public boolean getChannelCheck(String name)
	{
		if (!m_mapChannelLoginCheck.containsKey(name))
		{
			return true;
		}
		return m_mapChannelLoginCheck.get(name);
	}
	
	public boolean getGameAndChannelCheck(int key,int value)
	{
		if (!m_mapGameAndChannelLoginCheck.containsKey(key))
		{
			return true;
		}
		if(value==((Integer)m_mapGameAndChannelLoginCheck.get(key))){
			return false;
		}
		return true;
	}
	
	public int getStatisticsQuerySize()
	{
		return m_statisticsQuerySize;
	}
	
	public boolean getDoRequestStatistics()
	{
		return this.m_doRequestStatistics;
	}
	
	public int getHttpSocketTimeOut()
	{
		return this.m_httpSocketTimeOut;
	}
	
	public int getHttpConnectRequestTimeOut()
	{
		return m_httpConnectRequestTimeOut;
	}
	
	public int getHttpConnectTimeOut()
	{
		return this.m_httpConnectTimeOut;
	}
	
	public int getGameNotifyThreadsNum()
	{
		return m_gameNotifyThreadsNum;
	}
	
	public int getTalkingNotifyThreadsNum()
	{
		return m_talkingNotifyThreadsNum;
	}
	
	public String getTalkingDataUrl()
	{
		return m_talkingDataUrl;
	}
	public int getSignCheckTime()
	{
		return m_signCheckTime;
	}
	
	public String getReadDataSourcePath()
	{
		return m_readDataSourcePath;
	}
	
	public String getWriteDataSourcePath()
	{
		return m_writeDataSourcePath;
	}
	
	public String getSdkAesKey()
	{
		return m_sdkAesKey;
	}
	
	public String getSdkAesInitParam()
	{
		return m_sdkAesInitParam;
	}
	
	private void loadProperties(Properties props)
	{
		this.m_sdkAesKey = props.getProperty("platform.action.sdkaeskey"
				, "ytr9uyfdgd0op23w");
		this.m_sdkAesInitParam = props.getProperty("platform.action.aesinitparam"
				, "AES/ECB/PKCS5Padding");
		this.m_readDataSourcePath = props.getProperty("platform.core.readdatasourcepath"
				, "");
		this.m_writeDataSourcePath = props.getProperty("platform.core.writedatasourcepath"
				, "");
		this.m_signCheckTime = Integer.valueOf(props.getProperty("platform.core.signchecktime"
				, "1000"));
		this.m_gameNotifyThreadsNum = Integer.valueOf(props.getProperty("platform.core.gamenotifythreadsnum"
				, "3"));
		this.m_talkingNotifyThreadsNum = Integer.valueOf(props.getProperty("platform.core.talkingnotifythreadsnum"
				, "3"));
		this.m_httpConnectTimeOut = Integer.valueOf(props.getProperty("platform.core.httpconnectimeout"
				, "5000"));
		this.m_httpConnectRequestTimeOut = Integer.valueOf(props.getProperty("platform.core.httpconnectrequesttimeout"
				, "5000"));
		this.m_httpSocketTimeOut = Integer.valueOf(props.getProperty("platform.core.httpsockettimeout"
				, "10000"));
		this.m_doRequestStatistics = Boolean.valueOf(props.getProperty("platform.core.dorequeststatistics"
				, "false"));
		this.m_statisticsQuerySize = Integer.valueOf(props.getProperty("platform.core.statisticsquerysize"
				, "10000"));
		this.m_loggerServerUrl = props.getProperty("platform.core.loggerserverurl"
				, "");
		this.m_loggerServerUrlNew = props.getProperty("platform.core.loggerserverurlnew"
				, "");
		this.m_enableDoLogger = Boolean.valueOf(props.getProperty("platform.core.enabledologger"
				, "false"));
		this.m_maxCacheNum = Integer.valueOf(props.getProperty("platform.core.maxcachenum"
				, "100000"));
		String cannelCheck = props.getProperty("platform.core.channellogincheck"
				, "");
		String[] channels = cannelCheck.split(";");
		for (int i = 0; i < channels.length; i++)
		{
			String[] pairs = channels[i].split("-");
			String name = pairs[0];
			boolean check = Boolean.parseBoolean(pairs[1]);
			m_mapChannelLoginCheck.put(name, check);
		}
		String gameAndChannelCheck = props.getProperty("platform.core.gameandchannelforbid"
				, "");
		String[] gameAndChannels = gameAndChannelCheck.split(";");
		for (int i = 0; i < gameAndChannels.length; i++)
		{
			String[] pairs = gameAndChannels[i].split("-");
			int key = Integer.parseInt(pairs[0]);
			int value = Integer.parseInt(pairs[1]);
			m_mapGameAndChannelLoginCheck.put(key, value);
		}
		
		String appDuringime = props.getProperty("platform.core.appstoreorderduringtime");
		if (appDuringime != null)
		{
			this.m_appstoreOrderDuringTime = Integer.valueOf(appDuringime).intValue();
		}
		String appMaxNum = props.getProperty("platform.core.appstoreordermaxnum");
		if (appMaxNum != null)
		{
			this.m_appstoreOrderMaxNum = Integer.valueOf(appMaxNum).intValue();
		}
		
	}
	public boolean config(String cfgPath)
	{
		InputStream is = null;
		boolean rt = true;
		try
		{
			Properties props = new Properties();
			is = new FileInputStream(cfgPath);
			props.load(is);
			loadProperties(props);
		}
		catch (Exception e)
		{
			rt = false;
			System.out.println("account config failed e,{}"+e);
		}
		finally
		{
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch (Exception e)
				{
					
				}
			}
		}	
		return rt;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n-----------------PlatformGolbConfig------------------\n");
		stringBuilder.append("\nm_readDataSourcePath:  " + m_readDataSourcePath);
		stringBuilder.append("\nm_writeDataSourcePath  " + m_writeDataSourcePath);
		stringBuilder.append("\nm_sdkAesKey  " + m_sdkAesKey);
		stringBuilder.append("\nm_sdkAesInitParam " + m_sdkAesInitParam);
		stringBuilder.append("\nm_signCheckTime " + m_signCheckTime);
		stringBuilder.append("\nm_gameNotifyThreadsNum " + m_gameNotifyThreadsNum);
		stringBuilder.append("\nm_talkingNotifyThreadsNum " + m_talkingNotifyThreadsNum);
		stringBuilder.append("\nm_httpConnectTimeOut " + m_httpConnectTimeOut);
		stringBuilder.append("\nm_httpConnectRequestTimeOut " + m_httpConnectRequestTimeOut);
		stringBuilder.append("\nm_httpSocketTimeOut " + m_httpSocketTimeOut);
		stringBuilder.append("\nm_doRequestStatistics " + m_doRequestStatistics);
		stringBuilder.append("\nm_statisticsQuerySize " + m_statisticsQuerySize);
		stringBuilder.append("\nm_mapChannelLoginCheck " + m_mapChannelLoginCheck);
		stringBuilder.append("\nm_mapGameAndChannelLoginCheck " + m_mapGameAndChannelLoginCheck);
		stringBuilder.append("\nm_loggerServerUrl " + m_loggerServerUrl);
		stringBuilder.append("\nm_enableDoLogger " + m_enableDoLogger);
		stringBuilder.append("\nm_maxCacheNum " + m_maxCacheNum);
		stringBuilder.append("\nm_loggerServerUrlNew " + m_loggerServerUrlNew);
		stringBuilder.append("\nm_appstoreOrderDuringTime " + m_appstoreOrderDuringTime);
		stringBuilder.append("\nm_appstoreOrderMaxNum " + m_appstoreOrderMaxNum);
		stringBuilder.append("\n---------------------------------------------");
		
		return stringBuilder.toString();
	}
}
