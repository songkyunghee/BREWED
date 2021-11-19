package com.ssafy.cafe.controller.rest;

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

}
