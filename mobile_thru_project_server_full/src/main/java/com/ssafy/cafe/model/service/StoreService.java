package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

public interface StoreService {
	// 스토어 이름을 반환한다.
	public Map getStoreName(Integer sId);

	// 비콘 주소로 스토어 아이디를 반환한다.
	public Map getStoreId(String sBeacon);
	
	List<String> getBanner();

}
