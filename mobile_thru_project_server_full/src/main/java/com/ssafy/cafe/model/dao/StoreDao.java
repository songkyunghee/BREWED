package com.ssafy.cafe.model.dao;

import java.util.Map;

public interface StoreDao {
	
	// 해당하는 스토어 이름을 반환한다.
	Map getStoreName(Integer s_id);

}
