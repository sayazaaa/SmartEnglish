package com.smartenglishbackend.esrepo;

import com.smartenglishbackend.esentity.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
    @Query("""
            {
                "match":{
                    "title":{
                        "query":?0
                    }
                }
            }
            """)
   List<Article> searchArticlesByTitle(String title);
}
