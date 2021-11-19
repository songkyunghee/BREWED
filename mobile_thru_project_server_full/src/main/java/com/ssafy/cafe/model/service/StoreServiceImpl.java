package com.ssafy.cafe.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.StoreDao;

@Service
public class StoreServiceImpl implements StoreService {
	
	@Autowired
	StoreDao sDao;

	@Override
	public Map getStoreName(Integer s_id) {
		return sDao.getStoreName(s_id);
	}

}
