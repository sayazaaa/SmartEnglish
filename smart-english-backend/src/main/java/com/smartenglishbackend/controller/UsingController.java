package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.ModUseTimeStatistics;
import com.smartenglishbackend.dto.NoticeAPIResponse;
import com.smartenglishbackend.service.UseDataLogger;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/using")
public class UsingController {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UseDataLogger useDataLogger;
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> UpdateUsingTime(@RequestBody ModUseTimeStatistics modUseTimeStatistics,
                                          @RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            useDataLogger.AddUseTime(jwtUtils.getUserIdFromToken(token), modUseTimeStatistics.getModName(), modUseTimeStatistics.getValue());
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Long> GetMyUseTimeStatistics(@RequestParam(name="modname")String function,
                                                       @RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            return ResponseEntity.ok(useDataLogger.GetModUseTime(jwtUtils.getUserIdFromToken(token),function));
        }catch (RequestFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch(MyResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
