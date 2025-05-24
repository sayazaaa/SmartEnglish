package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.service.AccountService;
import com.smartenglishbackend.service.IAccountHandler;
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
        catch (AccountException e){
            return e.GetResponse();
        }
        catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PDTOAccount(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PDTOAccount(e.getMessage()));
        }
    }
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> Login(@RequestBody DTOAccount account) {
        try{
            IAccountHandler handler = accountService.getAccountHandler(account,"GET");
            return handler.Handle(account);
        }
        catch (AccountException e){
            return e.GetResponse();
        }
        catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PDTOAccount(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PDTOAccount(e.getMessage()));
        }
    }
}
