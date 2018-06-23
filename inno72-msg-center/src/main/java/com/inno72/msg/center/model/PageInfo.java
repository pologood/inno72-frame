package com.inno72.msg.center.model;

import lombok.Data;

/**
 * 返回page信息
 * @author sunlulu
 *
 * @param <T>
 */
@Data
public class PageInfo<T> {
	private T data;
	private int totalCount; // 总数
	private int pageNum = 1; //当前页
	private int pageSize = 10; //每页多少条
	private int totalPage = 0; //总页数
	public PageInfo(T data, int totalCount, int pageNum, int pageSize) {
		super();
		this.data = data;
		this.totalCount = totalCount;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalPage = this.getTotalPage();
	}
	
	public int getTotalPage() {
		double totalResults = new Integer(getTotalCount()).doubleValue();
		return (totalResults % getPageSize()==0)?new Double(Math.floor(totalResults / getPageSize())).intValue():(new Double(Math.floor(totalResults / getPageSize())).intValue()+1);
	}

	
}
