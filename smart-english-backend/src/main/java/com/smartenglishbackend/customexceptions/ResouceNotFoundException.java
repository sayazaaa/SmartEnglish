package com.smartenglishbackend.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResouceNotFoundException extends RuntimeException {
    public ResouceNotFoundException(String message) {
        super(message);
    }
    public ResponseEntity<Void> GetHttpResponse(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
