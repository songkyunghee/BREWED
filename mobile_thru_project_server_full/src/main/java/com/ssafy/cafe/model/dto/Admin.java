package com.ssafy.cafe.model.dto;

public class Admin {
	private String aId;
	private Integer sId;
	private String aName;
	private String aPass;
	
	public Admin(String aId, Integer sId, String aName, String aPass) {
		super();
		this.aId = aId;
		this.sId = sId;
		this.aName = aName;
		this.aPass = aPass;
	}
	
	public Admin() {}

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public Integer getsId() {
		return sId;
	}

	public void setsId(Integer sId) {
		this.sId = sId;
	}

	public String getaName() {
		return aName;
	}

	public void setaName(String aName) {
		this.aName = aName;
	}

	public String getaPass() {
		return aPass;
	}

	public void setaPass(String aPass) {
		this.aPass = aPass;
	}
	
	
}
