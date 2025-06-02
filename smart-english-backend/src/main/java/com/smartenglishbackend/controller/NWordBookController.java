package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTONWordBook;
import com.smartenglishbackend.dto.request.DTOUpdateNWordBook;
import com.smartenglishbackend.jpaentity.NWordBook;
import com.smartenglishbackend.service.NWordBookService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/nwordbook")
public class NWordBookController {

    @Autowired
    private NWordBookService nWordBookService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<NWordBook>> GetNWordBooks(@RequestHeader("Authorization") String token) {
        if(token == null || !jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(nWordBookService.getNWordBooks(jwtUtils.getUserIdFromToken(token)));
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(path="/detail",method = RequestMethod.GET)
    public ResponseEntity<List<String>> GetNWordBookDetail(@RequestParam(name="id")Integer id,
                                                           @RequestHeader("Authorization") String token) {
        if(token == null || !jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(nWordBookService.GetNWordBookContent(id));
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.PUT,consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> UpdateNWordBook(@RequestBody DTOUpdateNWordBook dtoUpdateNWordBook,
                                                @RequestHeader("Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            nWordBookService.UpdateNWordBook(dtoUpdateNWordBook,jwtUtils.getUserIdFromToken(token));
            return ResponseEntity.ok().build();
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AccountException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> CreateNWordBook(@RequestBody DTONWordBook dtoNWordBook,
                                                @RequestHeader("Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            nWordBookService.CreateNWordBook(dtoNWordBook,jwtUtils.getUserIdFromToken(token));
            return ResponseEntity.ok().build();
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/check")
    public ResponseEntity<Boolean> CheckNWordBook(@RequestParam(name="nwordbook_id") Integer nWordBookId,
                                                 @RequestParam(name="word")String word,
                                                 @RequestHeader("Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(nWordBookService.checkNWordBook(nWordBookId,word));
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
