package com.smartenglishbackend.esrepo;

import com.smartenglishbackend.dto.response.PDTOArticleInfo;
import com.smartenglishbackend.esentity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomArticleRepository {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    public List<PDTOArticleInfo> findAllInfo(){
        NativeQuery query = NativeQuery.builder().withQuery(
                q->q.matchAll(m->m)
        ).withSourceFilter(new FetchSourceFilter(new String[]{"_id", "title","cover","date","tags"}, null)).build();
        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        List<PDTOArticleInfo> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Article article = (Article) hit.getContent();
            PDTOArticleInfo articleInfo = new PDTOArticleInfo();
            articleInfo.setTitle(article.getTitle());
            articleInfo.setCover(article.getCover());
            articleInfo.setDate(article.getDate());
            List<String> tags = new ArrayList<>(article.getTags());
            articleInfo.setTags(tags);
            articleInfo.setId(article.getId());
            list.add(articleInfo);
        }
        return list;
    }
    public List<PDTOArticleInfo> searchByTitle(String title){
        NativeQuery query = NativeQuery.builder().withQuery(
                q->q.match(m->m.field("title").query(title))
        ).withSourceFilter(new FetchSourceFilter(new String[]{"_id", "title","cover","date","tags"}, null)).build();
        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        List<PDTOArticleInfo> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            Article article = (Article) hit.getContent();
            PDTOArticleInfo articleInfo = new PDTOArticleInfo();
            articleInfo.setTitle(article.getTitle());
            articleInfo.setCover(article.getCover());
            articleInfo.setDate(article.getDate());
            List<String> tags = new ArrayList<>(article.getTags());
            articleInfo.setTags(tags);
            articleInfo.setId(article.getId());
            list.add(articleInfo);
        }
        return list;
    }
}
