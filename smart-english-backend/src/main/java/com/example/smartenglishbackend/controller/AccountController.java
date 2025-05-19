package com.example.smartenglishbackend.controller;

import com.example.smartenglishbackend.dto.DTOAccount;
import com.example.smartenglishbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<String> createAccount(@RequestBody DTOAccount account) {
        try{
            if(accountService.GetRegistered(account)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account already exists");
            }
            if(account.getVerification() == null){
                return ResponseEntity.ok(accountService.getVerification(account));
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("format invalid");
    }
}
