package com.ssafy.cafe.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.FcmMessage;
import com.ssafy.cafe.model.dto.FcmMessage.Message;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Component
public class FcmService {
	
	public FcmService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public final ObjectMapper objectMapper;
	private final String API_URL = "https://fcm.googleapis.com/v1/projects/brewedcoffeeproject/messages:send";
	
	
	
	// FCM에 push 요청 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
	private String getAccessToken() throws IOException {
		String firebaseConfigPath = "firebase/brewed-firebase-adminsdk.json";
		
		// GoogleApi 사용 위해 oAuth2이용해 인증한 대상 나타내는 객체
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));		
	
		googleCredentials.refreshIfExpired();
		String token = googleCredentials.getAccessToken().getTokenValue();
		
		return token;
	}
	
	// FCM 알림 메시지 생성
	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
		// back ground
        Message message = new FcmMessage.Message(null, targetToken);
        
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        
        message.setData(data);
        
        FcmMessage fcmMessage = new FcmMessage(false, message);
        
        return objectMapper.writeValueAsString(fcmMessage);
	}
	
	// targetToken에 해당하는 device로 FCM 푸시 알림 전송
	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }
	

	@Autowired
	UserDao uDao;
	
	// 클라이언트 토큰...
	private List<String> clientTokens = new ArrayList<>();
	
	// 클라이언트 토큰 관리
	public void addToken(String token) {
		clientTokens.add(token);
	}
	
	// 등록된 모든 토큰으로 broadcasting
	public int broadCastMessage(String title, String body) throws IOException {
		clientTokens = uDao.selectUserToken();
		for (String token: clientTokens) {
			if (token != null) {
				System.out.println(token);
				sendMessageTo(token, title, body);
			}
		}
		
		return clientTokens.size();
	}
	
}
