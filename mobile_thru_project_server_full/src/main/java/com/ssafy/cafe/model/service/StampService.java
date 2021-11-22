package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Stamp;


public interface StampService {
    /**
     * id 사용자의 Stamp 이력을 반환한다.
     * @param id
     * @return
     */
	List<Map<String, Object>> selectByUser(String id);
	
	// 사용자 쿠폰과 스탬프 정보를 갱신한다.
	public int updateStamp(Stamp stamp);
	
	public int updateStampCoupon(Stamp stamp);
}
