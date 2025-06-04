package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.response.PDTOArticleInfo;
import com.smartenglishbackend.esentity.Article;
import com.smartenglishbackend.esrepo.ArticleRepository;
import com.smartenglishbackend.esrepo.CustomArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private CustomArticleRepository customArticleRepository;
    @Autowired
    private ArticleRepository articleRepository;
    public Article GetArticleDetail(String id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if(articleOptional.isEmpty()) {
            throw new MyResourceNotFoundException("Article not found");
        }
        return articleOptional.get();
    }
    public List<PDTOArticleInfo> SearchArticles(String searchStr) {
        System.out.println(searchStr);
        if(searchStr == null) {
            throw new RequestFormatException("Invalid Search String");
        }
        if(searchStr.isEmpty()) {
            return customArticleRepository.findAllInfo();
        }
        return customArticleRepository.searchByTitle(searchStr);
    }
    @Transactional
    public void DeleteArticle(String id) {
        if(id == null) {
            throw new RequestFormatException("Article not found");
        }
        Optional<Article> articleOptional = articleRepository.findById(id);
        if(articleOptional.isEmpty()) {
            throw new MyResourceNotFoundException("Article not found");
        }
        articleRepository.deleteById(id);
    }
    @Transactional
    public void CreateArticle(Article article) {
        if(article == null) {
            throw new RequestFormatException("Article cannot be null");
        }
        if(article.getContent() == null || article.getTitle() == null ||
        article.getTags() == null || article.getDate() == null ||
        article.getCover() == null) {
            throw new RequestFormatException("Invalid Article");
        }
        articleRepository.save(article);
    }
}
