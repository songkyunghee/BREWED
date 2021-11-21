package com.ssafy.cafe.model.dto;

public class Store {
    private Integer sId;
    private String sName;
    private String sBeacon;

    public Store(Integer sId, String sName, String sBeacon) {
        super();
        this.sId = sId;
        this.sName = sName;
        this.sBeacon = sBeacon;
    }

    public Integer getsId() {
        return sId;
    }
    public void setsId(Integer sId) {
        this.sId = sId;
    }
    public String getsName() {
        return sName;
    }
    public void setsName(String sName) {
        this.sName = sName;
    }
    public String getsBeacon() {
        return sBeacon;
    }

	public void setsBeacon(String sBeacon) {
	    this.sBeacon = sBeacon;
	}
}

