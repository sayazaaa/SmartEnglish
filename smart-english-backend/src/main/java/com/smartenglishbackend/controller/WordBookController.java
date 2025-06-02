package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOUpdateWordBook;
import com.smartenglishbackend.dto.request.DTOWordBook;
import com.smartenglishbackend.dto.response.PDTOWordBook;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.service.WordBookService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @RequestMapping(path = "/detail", method = RequestMethod.GET)
    public ResponseEntity<List<String>> GetWordBookDetails(@RequestParam int id, @RequestHeader("Authorization") String token){
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            WordBook wordBook = wordBookService.GetWordBookDetails(id);
            return ResponseEntity.ok(wordBook.getContent());
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PDTOWordBook>> GetWordBooks(@RequestHeader("Authorization") String token){
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(wordBookService.GetAllWordBook());
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> UpdateWordBook(@RequestHeader("Authorization") String token, @RequestBody DTOUpdateWordBook dtoUpdateWordBook){
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(jwtUtils.getUserIdFromToken(token) != -1){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            wordBookService.UpdateWordBook(dtoUpdateWordBook);
            return ResponseEntity.ok().build();
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeleteWordBook(@RequestHeader("Authorization") String token, @RequestParam Integer id){
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(jwtUtils.getUserIdFromToken(token) != -1){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            wordBookService.DeleteWordBook(id);
            return ResponseEntity.ok().build();
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
