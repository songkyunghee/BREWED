package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

public interface AdminDao {
	
	// store id에 해당하는 모든 주문내역을 반환한다.
	List<Map> getOrderList(Integer s_id);
	
	// store_id에 해당하는 완료된 모든 주문내역을 반환한다.
	List<Map> getDoneOrderList(Integer s_id);
	
	// store_Id에 해당하는 미완료된 모든 주문내역을 반환한다.
	List<Map> getNotDoneOrderList(Integer s_id);
}