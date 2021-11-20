package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Admin;

public interface AdminDao {
	
	
	// id에 해당하는 관리자 정보를 반환한다.
	Admin select(String a_id);
}