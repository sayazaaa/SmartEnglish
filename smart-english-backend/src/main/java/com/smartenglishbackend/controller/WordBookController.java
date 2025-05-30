package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOWordBook;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.service.WordBookService;
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
@RequestMapping(path = "/wordbook")
public class WordBookController {
    @Autowired
    private WordBookService wordBookService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> createWordBook(@RequestBody DTOWordBook dtoWordBook,
                                               @RequestHeader("Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int userId = jwtUtils.getUserIdFromToken(token);
        if(userId != -1){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try{
            wordBookService.CreateWordBook(dtoWordBook);
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }
}
