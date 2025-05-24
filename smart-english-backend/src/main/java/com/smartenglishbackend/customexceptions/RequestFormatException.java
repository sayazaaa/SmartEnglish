package com.smartenglishbackend.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RequestFormatException extends RuntimeException {
    public RequestFormatException() {
        super();
    }
    public RequestFormatException(String message) {
        super(message);
    }
    public ResponseEntity<Void> GetResponse(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
