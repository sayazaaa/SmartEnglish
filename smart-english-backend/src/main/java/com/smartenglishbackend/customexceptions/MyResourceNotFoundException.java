package com.smartenglishbackend.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MyResourceNotFoundException extends RuntimeException {
    public MyResourceNotFoundException(String message) {
        super(message);
    }
    public ResponseEntity<Void> GetHttpResponse(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
