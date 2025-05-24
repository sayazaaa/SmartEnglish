package com.smartenglishbackend.service;

import com.smartenglishbackend.dto.NoticeAPIResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class VerificationSender {
    private RestClient restClient;
    @Value("${notice.api}")
    private String baseUrl;
    @PostConstruct
    public void init() {
        restClient = RestClient.create(baseUrl);
    }
    public boolean Send(String verificationCode, String targetPhone) {
        NoticeAPIResponse res = restClient.get()
                .uri(uri -> uri.queryParam("key2",verificationCode)
                        .queryParam("targets",targetPhone).build())
                .retrieve()
                .body(NoticeAPIResponse.class);
        if(res == null){
            throw new RuntimeException("Error sending verification code");
        }
        Integer status = res.getCode();
        return (status != null) && status.equals(HttpStatus.OK.value());
    }
}
