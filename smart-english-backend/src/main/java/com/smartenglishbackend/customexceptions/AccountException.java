package com.smartenglishbackend.customexceptions;

import com.smartenglishbackend.dto.response.PDTOAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AccountException extends RuntimeException {
    public AccountException() {
        super();
    }
    public AccountException(String message) {
        super(message);
    }
    public ResponseEntity<PDTOAccount> GetResponse(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PDTOAccount(getMessage()));
    }
}
