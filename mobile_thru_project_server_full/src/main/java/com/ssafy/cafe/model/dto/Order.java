package com.ssafy.cafe.model.dto;

import java.util.Date;
import java.util.List;


public class Order {
    private Integer id;
    private String userId;
    private Integer storeId;
    private String orderTable;
    private Date orderTime;

    private Character completed;
    private List<OrderDetail> details;
    
    public Order(Integer id, String userId, Integer storeId, String orderTable, Date orderTime, Character complited) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.orderTable = orderTable;
        this.orderTime = orderTime;
        this.completed = complited;
    }

    public Order(String userId, Integer storeId, String orderTable, Date orderTime, Character complited) {
    	this.storeId = storeId;
        this.userId = userId;
        this.orderTable = orderTable;
        this.orderTime = orderTime;
        this.completed = complited;
    }
    
    public Order() {}

        
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(String orderTable) {
		this.orderTable = orderTable;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Character getCompleted() {
		return completed;
	}

	public void setCompleted(Character completed) {
		this.completed = completed;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", storeId=" + storeId + ", orderTable=" + orderTable + ", orderTime=" + orderTime
				+ ", completed=" + completed + ", details=" + details + "]";
	}
    
    
}
