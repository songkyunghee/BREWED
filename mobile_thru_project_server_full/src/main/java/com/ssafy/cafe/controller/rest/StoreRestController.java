package com.ssafy.cafe.controller.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.service.StoreService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/store")
@CrossOrigin("*")
public class StoreRestController {
	
	@Autowired
	StoreService sService;
	
	@GetMapping("/{storeId}")
	@ApiOperation(value="{storeId}에 해당하는 스토어의 이름을 반환한다.", response=String.class)
	public Map getStoreName(@PathVariable Integer storeId) {
		return sService.getStoreName(storeId);
	}
	
	@GetMapping("/beacon/{beacon}")
	@ApiOperation(value="beacon의 주소에 해당하는 스토어의 ID를 반환한다.", response=String.class)
	public Map getStoreId(@PathVariable String beacon) {
	    return sService.getStoreId(beacon);
	}
	
	@GetMapping("/banner")
	@ApiOperation(value="전체 배너 이미지를 반환한다.", response = String.class)
	public List<String> getAllBanner() {
		return sService.getBanner();
	}

}
