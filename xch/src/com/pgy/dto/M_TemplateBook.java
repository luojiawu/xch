package com.pgy.dto;

public class M_TemplateBook {
	
	private int templateBookId;
	
	private String templateName;
	
	private String templateCover;
	
	private int photoNum;

	public int getTemplateBookId() {
		return templateBookId;
	}

	public void setTemplateBookId(int templateBookId) {
		this.templateBookId = templateBookId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateCover() {
		return templateCover;
	}

	public void setTemplateCover(String templateCover) {
		this.templateCover = templateCover;
	}

	public int getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}
	
}
