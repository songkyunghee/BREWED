package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;


public interface StoreDao {
	
	// store_id에 해당하는 스토어의 이름을 반환한다.
	Map getStoreName(Integer s_id);

	// store beacon의 주소로 store id를 반환한다.
	Map getStoreId(String sBeacon);
	
	// 배너 이미지를 반환한다.
	List<String> selectAllBanner();
	

}
