package com.smartenglishbackend.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.request.DTOAdmin;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.service.AccountService;
import com.smartenglishbackend.service.AdminLoginService;
import com.smartenglishbackend.service.IAccountHandler;
import com.smartenglishbackend.service.UserService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AdminLoginService adminLoginService;
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> createAccount(@RequestBody DTOAccount account) {
        try{
            IAccountHandler handler = accountService.getAccountHandler(account,"POST");
            if(handler == null){
                throw new RequestFormatException("invalid request");
            }
            return handler.Handle(account);
        }
        catch (AccountException e){
            return e.GetResponse();
        }
        catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PDTOAccount(e.getMessage()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(path="/login", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> Login(@RequestBody DTOAccount account) {
        try{
            IAccountHandler handler = accountService.getAccountHandler(account,"LOGIN");
            if(handler == null){
                throw new RequestFormatException("invalid request");
            }
            return handler.Handle(account);
        }
        catch (AccountException e){
            return e.GetResponse();
        }
        catch (RequestFormatException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PDTOAccount(e.getMessage()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method=RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> updatePassword(@RequestBody DTOAccount account) {
        try{
            IAccountHandler handler = accountService.getAccountHandler(account,"PUT");
            if(handler == null){
                throw new RequestFormatException("invalid request");
            }
            return handler.Handle(account);
        }
        catch (AccountException e){
            return e.GetResponse();
        }
        catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PDTOAccount(e.getMessage()));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(path = "/admin",method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOAccount> adminLogin(@RequestBody DTOAdmin admin) {
        try{
            boolean res = adminLoginService.adminLogin(admin);
            if(!res){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok().header("Authorization", adminLoginService.getToken())
                    .body(new PDTOAccount("Login Succeed"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
