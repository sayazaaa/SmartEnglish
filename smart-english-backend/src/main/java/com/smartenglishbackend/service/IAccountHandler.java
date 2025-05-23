package com.smartenglishbackend.service;

import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import org.springframework.http.ResponseEntity;

public interface IAccountHandler {
    boolean accept(DTOAccount dtoAccount, String method);
    ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount);
}
