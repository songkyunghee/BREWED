package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Admin;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.User;

public interface AdminService {

	
	// 조회된 관리자 정보를 반환한다.
    public Admin login(String a_id, String a_pass);
    
    // 어드민 정보를 갱신한다.
    public int update(Admin admin);
    
    // store id에 해당하는 admin의 토큰 값을 반환한다.
    public Map selectAdmin(String a_id);
}
