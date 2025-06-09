package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.jpaentity.Feedback;
import com.smartenglishbackend.service.FeedBackService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/feedback")
public class FeedBackController {
    @Autowired
    private FeedBackService feedBackService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.POST,consumes = "application/json;charset-8")
    public ResponseEntity<Void> CreateFeedback(@RequestBody Feedback feedback,
                                               @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            feedBackService.addFeedback(feedback, jwtUtils.getUserIdFromToken(token));
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Feedback>> GetFeedbacks(@RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            return ResponseEntity.ok(feedBackService.getAllFeedback());
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeleteFeedBack(@RequestParam(name="id") Integer id, @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            feedBackService.removeFeedback(id);
            return ResponseEntity.ok().build();
        }catch(MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
