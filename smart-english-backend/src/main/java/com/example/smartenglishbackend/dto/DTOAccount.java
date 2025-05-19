package com.example.smartenglishbackend.dto;

import com.example.smartenglishbackend.entity.Account;
import com.example.smartenglishbackend.jparepo.AccountReposity;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Data
public class DTOAccount {
    private String phone;
    private String verification;
    private String password;
}
