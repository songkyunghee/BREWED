package com.ssafy.cafe.model.dto;

public class Admin {
	private String aId;
	private Integer sId;
	private String aName;
	private String aPass;
	private String aToken;
	
	public Admin(String aId, Integer sId, String aName, String aPass, String aToken) {
		super();
		this.aId = aId;
		this.sId = sId;
		this.aName = aName;
		this.aPass = aPass;
		this.aToken = aToken;
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

	public String getaToken() {
		return aToken;
	}

	public void setaToken(String aToken) {
		this.aToken = aToken;
	}
	
	
	
	
}
