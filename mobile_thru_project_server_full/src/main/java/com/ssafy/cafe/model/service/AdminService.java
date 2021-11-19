package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Order;

public interface AdminService {

	// store id에 해당하는 모든 주문 내역을 반환한다.
	public List<Map> getOrderList(Integer s_id);
	
	// store id에 해당하는 완성된 주문 내역을 반환한다.
	public List<Map> getDoneOrderList(Integer s_id);
	
	// store id에 해당하는 미완료된 주문 내역을 반환한다.
	public List<Map> getNotDoneOrderList(Integer s_id);
}
