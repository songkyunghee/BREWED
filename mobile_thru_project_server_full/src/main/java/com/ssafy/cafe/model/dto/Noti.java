package com.ssafy.cafe.model.dto;

import java.util.Date;

public class Noti {
	private Integer nId;
	private String nMsg;
	private Date notiTime;
	private String userId;
	
	public Noti(Integer nId, String nMsg, Date notiTime, String userId) {
		this.nId = nId;
		this.nMsg = nMsg;
		this.notiTime = notiTime;
		this.userId = userId;
	}
	
	public Noti() {
		
	}
	
	public Integer getnId() {
		return nId;
	}
	public void setnId(Integer nId) {
		this.nId = nId;
	}
	public String getnMsg() {
		return nMsg;
	}
	public void setnMsg(String nMsg) {
		this.nMsg = nMsg;
	}
	public Date getNotiTime() {
		return notiTime;
	}
	public void setNotiTime(Date notiTime) {
		this.notiTime = notiTime;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
