package com.pgy.common;



public class Entry
{
	
	private static Entry s_instance = null;
	private Config m_config = null;
	
	private Entry()
	{
		m_config = new Config();
	}
	
	public static Entry getInstance()
	{
		if (s_instance == null)
		{
			s_instance = new Entry();
		}
		return s_instance;
	}
	
	public Config getConfig()
	{
		return m_config;
	}
	
	public boolean config(String path)
	{
		boolean result = m_config.config(path);
		System.out.println(m_config.toString());
		return result;
	}
}
