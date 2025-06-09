package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.BasicLogData;
import com.smartenglishbackend.dto.request.DTOCalcDataReq;
import com.smartenglishbackend.service.UseDataLogger;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/data")
public class DataController {
    @Autowired
    private UseDataLogger useDataLogger;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<BasicLogData> getBasicLogData(@RequestHeader("Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try{
            return ResponseEntity.ok(useDataLogger.GetBasicLogData());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public ResponseEntity<List<Float>> getCalcData(@RequestBody DTOCalcDataReq request,
                                                         @RequestHeader("Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try{
            return ResponseEntity.ok(useDataLogger.GetCalcData(request));
        }catch (RequestFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
