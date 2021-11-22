package com.ssafy.cafe.model.dto;

public class Coupon {
	private Integer cId;
	private Integer cValue;
	private String userId;
	
	public Coupon() {}
	
	public Coupon(Integer cId, Integer cValue, String userId) {
		this.cId = cId;
		this.cValue = cValue;
		this.userId = userId;
	}

	public Integer getcId() {
		return cId;
	}

	public void setcId(Integer cId) {
		this.cId = cId;
	}

	public Integer getcValue() {
		return cValue;
	}

	public void setcValue(Integer cValue) {
		this.cValue = cValue;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
