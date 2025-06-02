package com.smartenglishbackend.esrepo;

import com.smartenglishbackend.esentity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, String> {
}
