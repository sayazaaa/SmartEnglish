package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.response.PDTOArticleInfo;
import com.smartenglishbackend.esentity.Article;
import com.smartenglishbackend.service.ArticleService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Article> getArticle(@RequestParam(name = "id") String id,
                                              @RequestHeader(name="Authorization") String token) {
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(articleService.GetArticleDetail(id));
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public ResponseEntity<List<PDTOArticleInfo>> SearchArticle(@RequestParam(name = "query_string") String str,
                                                               @RequestHeader(name="Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            return ResponseEntity.ok(articleService.SearchArticles(str));
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeleteArticle(@RequestParam(name = "id") String id,
                                              @RequestHeader(name="Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            articleService.DeleteArticle(id);
            return ResponseEntity.ok().build();
        }catch (MyResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> CreateArticle(@RequestBody Article article,
                                              @RequestHeader(name="Authorization") String token){
        if(!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!jwtUtils.getUserIdFromToken(token).equals(-1)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            articleService.CreateArticle(article);
            return ResponseEntity.ok().build();
        }catch (RequestFormatException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
