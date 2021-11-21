package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.AdminDao;
import com.ssafy.cafe.model.dto.Admin;
import com.ssafy.cafe.model.dto.User;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDao aDao;


	@Override
	public Admin login(String a_id, String a_pass) {
		Admin admin = aDao.select(a_id);
		System.out.println("sevice impl" + admin);
        if (admin != null && admin.getaPass().equals(a_pass)) {
        	
            return admin;
        } else {
            return null;
        }
	}


	@Override
	public int update(Admin admin) {
		return aDao.update(admin);
	}


	@Override
	public Map selectAdmin(String s_id) {
		return aDao.selectAdmin(s_id);
	}
	
	
	

}
