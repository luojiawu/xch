package com.pgy.dto;

/**
 * 类型传输类
 * @author Administrator
 *
 */
public class Type {

	/**
	 * 类型id
	 */
	private int typeId;
	/**
	 * SKU规格
	 */
	private String typeName;
	/**
	 * 规格尺寸
	 */
	private String typeSize;
	/**
	 * 默认纸张，没有的为0
	 */
	private int defaultPaper;
	/**
	 * 默认纸张价格
	 */
	private double defaultPrice;
	/**
	 * 单页价格
	 */
	private double unitPrice;
	/**
	 * 主图轮播图
	 */
	private String mainImg;
	/**
	 * 详情图
	 */
	private String detailsImg;
	/**
	 * 轮播图数组
	 */
	private String[] mainImgs;
	
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeSize() {
		return typeSize;
	}
	public void setTypeSize(String typeSize) {
		this.typeSize = typeSize;
	}
	public int getDefaultPaper() {
		return defaultPaper;
	}
	public void setDefaultPaper(int defaultPaper) {
		this.defaultPaper = defaultPaper;
	}
	public double getDefaultPrice() {
		return defaultPrice;
	}
	public void setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public String getDetailsImg() {
		return detailsImg;
	}
	public void setDetailsImg(String detailsImg) {
		this.detailsImg = detailsImg;
	}
	public String[] getMainImgs() {
		return mainImgs;
	}
	public void setMainImgs(String[] mainImgs) {
		this.mainImgs = mainImgs;
	}
	
}
