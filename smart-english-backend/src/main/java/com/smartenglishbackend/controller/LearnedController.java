package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOUpdateLearned;
import com.smartenglishbackend.dto.response.PDTOLearned;
import com.smartenglishbackend.dto.response.PDTOUpdateLearned;
import com.smartenglishbackend.service.LearnedService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/learned")
public class LearnedController {
    @Autowired
    private LearnedService learnedService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=utf-8")
    public ResponseEntity<PDTOUpdateLearned> UpdateLearned(@RequestBody DTOUpdateLearned dtoUpdateLearned,
                                                           @RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            String newWord = learnedService.UpdateLearned(dtoUpdateLearned, jwtUtils.getUserIdFromToken(token));
            if(newWord.isEmpty()){
                return ResponseEntity.ok(new PDTOUpdateLearned(newWord,"final"));
            }
            return ResponseEntity.ok(new PDTOUpdateLearned(newWord,"success"));
        }catch(AccountException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(RequestFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PDTOLearned>> getAllLearned(@RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(learnedService.getAllLearned(jwtUtils.getUserIdFromToken(token)));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
