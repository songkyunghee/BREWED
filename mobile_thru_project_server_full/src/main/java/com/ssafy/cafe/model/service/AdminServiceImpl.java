package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.AdminDao;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDao aDao;

	@Override
	public List<Map> getOrderList(Integer s_id) {
		return aDao.getOrderList(s_id);
	}

	@Override
	public List<Map> getDoneOrderList(Integer s_id) {
		return aDao.getDoneOrderList(s_id);
	}

	@Override
	public List<Map> getNotDoneOrderList(Integer s_id) {
		return aDao.getNotDoneOrderList(s_id);
	}
	

}
