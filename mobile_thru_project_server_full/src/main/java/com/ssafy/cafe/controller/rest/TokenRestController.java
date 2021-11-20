package com.ssafy.cafe.controller.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.service.FcmService;

@RestController
@CrossOrigin("*")
public class TokenRestController {
	
	@Autowired
	FcmService fService;
	
	@PostMapping("/token")
	public String registToken(String token) {
		fService.addToken(token);
		return token;
	}
	
	@PostMapping("/broadcast")
	public Integer broadCast(String title, String body) throws IOException {
		
		return fService.broadCastMessage(title, body);
	}
	
	@PostMapping("/sendMessageTo")
	public void sendMessageTo(String token, String title, String body) throws IOException {
		fService.sendMessageTo(token, title, body);
	}

}
