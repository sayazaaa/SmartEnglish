package com.example.smartenglishbackend.controller;

import com.example.smartenglishbackend.dto.request.DTOAccount;
import com.example.smartenglishbackend.dto.response.PDTOAccount;
import com.example.smartenglishbackend.service.AccountService;
import com.example.smartenglishbackend.service.IAccountHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> createAccount(@RequestBody DTOAccount account) {
        try{
            IAccountHandler handler = accountService.getAccountHandler(account,"POST");
            return handler.Handle(account);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PDTOAccount(e.getMessage()));
        }
    }
}
