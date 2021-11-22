package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Stamp;

public interface StampDao {
    int insert(Stamp stamp);

    Stamp select(Integer stampId);

    List<Stamp> selectAll();
    
    List<Map<String, Object>> selectByUserId(String userId);
}
