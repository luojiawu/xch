package com.pgy.dto;

public class M_Template {
	
	private int templateId;
	
	private int templateBookId;
	
	private String doc;
	
	private int width;
	
	private int height;
	
	private String backgroundUrl;

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getTemplateBookId() {
		return templateBookId;
	}

	public void setTemplateBookId(int templateBookId) {
		this.templateBookId = templateBookId;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	
}
