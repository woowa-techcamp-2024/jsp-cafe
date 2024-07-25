package org.example.repository;

import java.util.List;
import org.example.entity.Article;

public interface ArticleRepository {


    Article save(Article article);
    List<Article> findAll();
}
