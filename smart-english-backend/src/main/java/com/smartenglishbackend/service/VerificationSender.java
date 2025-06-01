package com.smartenglishbackend.service;

import com.smartenglishbackend.dto.NoticeAPIResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VerificationSender {
    private RestTemplate restTemplate;
    @Value("${notice.api}")
    private String baseUrl;
    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }
    public boolean Send(String verificationCode, String targetPhone) {
        String url = String.format("%s?key2=%s&targets=%s", baseUrl, verificationCode, targetPhone);
        ResponseEntity<NoticeAPIResponse> response = restTemplate.getForEntity(url, NoticeAPIResponse.class);
        NoticeAPIResponse res = response.getBody();
        if(res == null){
            throw new RuntimeException("Error sending verification code");
        }
        Integer status = res.getCode();
        return (status != null) && status.equals(HttpStatus.OK.value());
    }
}
