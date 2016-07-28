package com.demo.charile.dianping.entity;


public class ResponseObject<T> {

	private String state;
	private T datas;
	private int page;
	private int size;
	private int count;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ResponseObject() {
		
	}
	
	public ResponseObject(String state, T datas) {
		super();
		this.state = state;
		this.datas = datas;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}
	
	
	
}
