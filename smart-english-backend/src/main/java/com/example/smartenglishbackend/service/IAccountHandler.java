package com.example.smartenglishbackend.service;

import com.example.smartenglishbackend.dto.request.DTOAccount;
import com.example.smartenglishbackend.dto.response.PDTOAccount;
import org.springframework.http.ResponseEntity;

public interface IAccountHandler {
    boolean accept(DTOAccount dtoAccount, String method);
    ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount);
}
