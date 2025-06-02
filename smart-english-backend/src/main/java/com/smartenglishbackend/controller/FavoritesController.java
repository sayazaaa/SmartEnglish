package com.smartenglishbackend.controller;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOFavoritesSetAddArticle;
import com.smartenglishbackend.dto.response.PDTOArticleInfo;
import com.smartenglishbackend.jpaentity.FavoritesSet;
import com.smartenglishbackend.service.FavoritesService;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favorite")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private JWTUtils jwtUtils;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FavoritesSet>> GetFavoritesSets(@RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(favoritesService.GetSetList(jwtUtils.getUserIdFromToken(token)));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/detail")
    public ResponseEntity<List<PDTOArticleInfo>> GetFavoritesSetDetail(@RequestParam(name="id") Integer id, @RequestHeader(name="Authorization") String token) {
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(favoritesService.GetFavoritesDetail(jwtUtils.getUserIdFromToken(token), id));
        }catch (MyResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AccountException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> CreateFavoritesSet(@RequestBody FavoritesSet favoritesSet, @RequestHeader(name="Authorization") String token){
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            favoritesService.CreateFavoritesSet(favoritesSet, jwtUtils.getUserIdFromToken(token));
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json;charset=utf-8")
    public ResponseEntity<Void> AddRemoveArticleToFavoritesSet(@RequestBody DTOFavoritesSetAddArticle dtoFavoritesSetAddArticle, @RequestHeader(name="Authorization") String token){
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            favoritesService.AddRemoveArticleToFavoritesSet(dtoFavoritesSetAddArticle.getFavoritesSetId(),
                    jwtUtils.getUserIdFromToken(token), dtoFavoritesSetAddArticle.getArticle(),
                    dtoFavoritesSetAddArticle.getMethod());
            return ResponseEntity.ok().build();
        }catch (MyResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AccountException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeleteFavoritesSet(@RequestParam(name="id") Integer id,@RequestHeader(name="Authorization") String token){
        if (!jwtUtils.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            favoritesService.RemoveFavoritesSet(id, jwtUtils.getUserIdFromToken(token));
            return ResponseEntity.ok().build();
        }catch (MyResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (AccountException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(method = RequestMethod.GET, path = "/check")
    public ResponseEntity<Boolean> CheckFavoritesSet(@RequestParam(name="id") Integer id,
                                                     @RequestParam(name="article") String article,
                                                     @RequestHeader(name="Authorization") String token){
            if (!jwtUtils.verifyToken(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            try{
                return ResponseEntity.ok(favoritesService.checkFavorites(id,article));
            }catch (RequestFormatException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }catch (AccountException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
}
