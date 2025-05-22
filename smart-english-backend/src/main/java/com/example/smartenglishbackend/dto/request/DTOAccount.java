package com.example.smartenglishbackend.dto.request;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Data
public class DTOAccount {
    private String phone;
    private String verification;
    private String password;
}
