package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.Article;

public interface ArticleRepository {


    Article save(Article article);
    List<Article> findAll();
    Optional<Article> findById(int i);
}
