package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.jpaentity.SWord;
import com.smartenglishbackend.service.WordSetService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/wordset")
public class WordSetController {
    @Autowired
    private WordSetService wordSetService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<SWord>> GetWordSet(@RequestParam(name="type", required=true) String type,
                                           @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int id = jwtUtils.getUserIdFromToken(token);
        try{
            return ResponseEntity.ok(wordSetService.getWordSet(type,id));
        }catch (AccountException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=utf-8")
    ResponseEntity<Void> UpdateWordSet(@RequestParam(name="type", required = true)String type,
                                       @RequestBody List<SWord> words,
                                       @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int id = jwtUtils.getUserIdFromToken(token);
        try{
            wordSetService.UpdateWordSet(words,type,id);
            return ResponseEntity.ok().build();
        }catch (AccountException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
