package com.ssafy.cafe.controller.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Admin;
import com.ssafy.cafe.model.service.AdminService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/admin")
@CrossOrigin("*")
public class AdminRestController {
	
	@Autowired
	private AdminService aService;
	
	
	@PostMapping("/login")
    @ApiOperation(value = "로그인 처리 후 성공적으로 로그인 되었다면 loginId라는 쿠키를 내려보낸다.", response = Admin.class)
    public Admin login(@RequestBody Admin admin, HttpServletResponse response) throws UnsupportedEncodingException {
        Admin selected = aService.login(admin.getaId(), admin.getaPass());
        if (selected != null) {
            Cookie cookie = new Cookie("loginAdminId", URLEncoder.encode(selected.getaId(), "utf-8"));
            cookie.setMaxAge(1000 * 1000);
            response.addCookie(cookie);
        }else {
        	selected = new Admin();
        }
        System.out.println(selected.getaName() + " " + admin);
        return selected;
    }
	
	@PutMapping("/update")
	@ApiOperation(value = "어드민 정보를 갱신한다.", response = Integer.class)
	public int update(@RequestBody Admin admin) {
		System.out.println(admin);
		return aService.update(admin);
	}


}
