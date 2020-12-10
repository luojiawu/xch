package com.pgy.common;
/**
 * 分页实体
 * @author Administrator
 *
 */
public class PageModel {
	/** 定义默认一页显示的记录条数 */
	private static final int PAGE_SIZE = 10;
	/** 当前页码 */
	private int pageIndex;
	/** 一页显示的数量 */
	private int pageSize;
	/** 总记录条数 */
	private int recordCount;

	/**计算当前页*/
	public int getPageIndex() {
		this.pageIndex = pageIndex > getTotalSize() ? this.getTotalSize() : this.pageIndex;
		return pageIndex <= 1 ? 1 : pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize <= 1 ? PAGE_SIZE : pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getTotalSize() {
		return ((getRecordCount() - 1) / getPageSize()) + 1;
	}
	/** limit ?,?： 第一个?的值 */
	public int getStartRow() {
		return (this.getPageIndex() - 1) * this.getPageSize();
	}
	
}
