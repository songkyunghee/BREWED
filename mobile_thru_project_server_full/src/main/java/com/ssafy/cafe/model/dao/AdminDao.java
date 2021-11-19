package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

public interface AdminDao {
	
	// store id에 해당하는 Order List를 반환한다.
	List<Map> getOrderList(Integer s_id);
	
	// store_id에 해당하는 완료된 Order List를 반환한다.
	List<Map> getDoneOrderList(Integer s_id);
	
	// store_Id에 해당하는 미완료된 Order List를 반환한다.
	List<Map> getNotDoneOrderList(Integer s_id);
}
