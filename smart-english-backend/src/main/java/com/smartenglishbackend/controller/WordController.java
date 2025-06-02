package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.esentity.Word;
import com.smartenglishbackend.service.WordService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Word> GetWordDetail(@RequestParam(required = true) String word,
                                              @RequestHeader(name="Authorization")String token) {
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(wordService.GetWordDetail(word));
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public ResponseEntity<List<Word>> SearchWords(@RequestParam(name = "query_string",required = true) String queryString,
                                                  @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(wordService.SearchWords(queryString));
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> UpdateWord(Word word, @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try{
            wordService.SaveWord(word);
            return ResponseEntity.ok().build();
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public ResponseEntity<List<Word>> GetAllWords(@RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            return ResponseEntity.ok(wordService.GetAllWords());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
