package com.bt.shopguide.dao.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageDataVo<T> {
	private List<T> data;
	private int pageIndex = 1;
	private int pageSize = 20;
	private int totalCount = 0;
	private int totalPage = 1;
	private Map<String,Object> conditionMap;
	
	public PageDataVo(){
		conditionMap = new HashMap<String, Object>();
	}
	
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = this.totalCount / this.pageSize + 1;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public Map<String, Object> getConditionMap() {
		return conditionMap;
	}
	public void setConditionMap(Map<String, Object> conditionMap) {
		this.conditionMap = conditionMap;
	}
}
