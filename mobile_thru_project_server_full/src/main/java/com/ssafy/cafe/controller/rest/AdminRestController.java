package com.ssafy.cafe.controller.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.service.AdminService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/admin")
@CrossOrigin("*")
public class AdminRestController {
	
	@Autowired
	private AdminService aService;
	
	@GetMapping("/{storeId}")
	@ApiOperation(value="{storeId}에 해당하는 모든 주문을 목록 형태로 반환한다.", response=List.class)
	public List<Map> getOrderList(@PathVariable Integer storeId) {
		return aService.getOrderList(storeId);
	}
	
	@GetMapping("/doneList/{storeId}")
	@ApiOperation(value="{storeId}에 해당하는 완료된 모든 주문을 목록 형태로 반환한다.", response=List.class)
	public List<Map> getDoneOrderList(@PathVariable Integer storeId) {
		return aService.getDoneOrderList(storeId);
	}
	
	@GetMapping("/notdoneList/{storeId}")
	@ApiOperation(value="{storeId}에 해당하는 미완료된 모든 주문을 목록 형태로 반환한다.", response=List.class)
	public List<Map> getNotDoneOrderList(@PathVariable Integer storeId) {
		return aService.getNotDoneOrderList(storeId);
	}

	// 충돌테스트
	// 1


}
