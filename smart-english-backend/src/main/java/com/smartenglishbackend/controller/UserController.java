package com.smartenglishbackend.controller;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.dto.request.DTOUser;
import com.smartenglishbackend.dto.response.PDTOUser;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.service.UserService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PDTOUser> GetUser(@RequestHeader("Authorization")String token){
        boolean result = jwtUtils.verifyToken(token);
        if(!result){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return userService.getUser(jwtUtils.getUserIdFromToken(token));
        }catch (AccountException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<PDTOUser> UpdateUser(@RequestHeader("Authorization")String token, @RequestBody DTOUser dtoUser){
        boolean result = jwtUtils.verifyToken(token);
        if(!result){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return userService.updateUser(dtoUser,jwtUtils.getUserIdFromToken(token));
        }catch (AccountException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
