package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.dto.response.PDTOArticleInfo;
import com.smartenglishbackend.esrepo.ArticleRepository;
import com.smartenglishbackend.esrepo.CustomArticleRepository;
import com.smartenglishbackend.jpaentity.Favorites;
import com.smartenglishbackend.jpaentity.FavoritesId;
import com.smartenglishbackend.jpaentity.FavoritesSet;
import com.smartenglishbackend.jparepo.FavoritesRepository;
import com.smartenglishbackend.jparepo.FavoritesSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritesService {
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private FavoritesSetRepository favoritesSetRepository;
    @Autowired
    private CustomArticleRepository customArticleRepository;
    public List<FavoritesSet> GetSetList(Integer userId){
        List<FavoritesSet> favoritesSetList = favoritesSetRepository.findByAccountId(userId);
        for (FavoritesSet favoritesSet : favoritesSetList) {
            favoritesSet.setAccountId(null);
        }
        return favoritesSetList;
    }
    public List<PDTOArticleInfo> GetFavoritesDetail(Integer userId, Integer setId){
        Optional<FavoritesSet> favoritesSet = favoritesSetRepository.findById(setId);
        if(favoritesSet.isEmpty()){
            throw new MyResourceNotFoundException("FavoritesSet not found");
        }
        FavoritesSet set = favoritesSet.get();
        if(!set.getAccountId().equals(userId)){
            throw new AccountException("wrong account id");
        }
        List<Favorites> favoritesList = favoritesRepository.findBySetId(setId);
        List<PDTOArticleInfo> pDTOArticleInfoList = new ArrayList<>();
        for(Favorites favorites : favoritesList){
            PDTOArticleInfo pDTOArticleInfo1 = customArticleRepository.findById(favorites.getArticleId());
            if(pDTOArticleInfo1 != null)pDTOArticleInfoList.add(pDTOArticleInfo1);
        }
        return pDTOArticleInfoList;
    }
    public void CreateFavoritesSet(FavoritesSet favoritesSet, Integer userId){
        favoritesSet.setAccountId(userId);
        favoritesSetRepository.save(favoritesSet);
    }
    public void AddRemoveArticleToFavoritesSet(Integer favoriteSetId,Integer userId, String articleId, String method){
        Optional<FavoritesSet> favoritesSet = favoritesSetRepository.findById(favoriteSetId);

        if(favoritesSet.isEmpty()){
            throw new MyResourceNotFoundException("FavoritesSet not found");
        }
        FavoritesSet favoritesSet1 = favoritesSet.get();
        if(!favoritesSet1.getAccountId().equals(userId)){
            throw new AccountException("wrong account id");
        }
        Favorites favorites = new Favorites();
        favorites.setArticleId(articleId);
        favorites.setSetId(favoriteSetId);
        if(method.equals("add")){
            PDTOArticleInfo articleInfo = customArticleRepository.findById(articleId);
            if(articleInfo == null){
                throw new MyResourceNotFoundException("Article not found");
            }
            favoritesRepository.save(favorites);
        }else if(method.equals("remove")){
            Optional<Favorites> favoritesOptional = favoritesRepository.findById(new FavoritesId(favoriteSetId,articleId));
            if(favoritesOptional.isEmpty()){
                throw new MyResourceNotFoundException("Favorites not found");
            }
            favoritesRepository.delete(favorites);
        }
    }
    public void RemoveFavoritesSet(Integer favoriteSetId, Integer userId){
        Optional<FavoritesSet> favoritesSet = favoritesSetRepository.findById(favoriteSetId);
        if(favoritesSet.isEmpty()){
            throw new MyResourceNotFoundException("FavoritesSet not found");
        }
        if(!favoritesSet.get().getAccountId().equals(userId)){
            throw new AccountException("wrong account id");
        }
        favoritesSetRepository.deleteById(favoriteSetId);
    }
}
