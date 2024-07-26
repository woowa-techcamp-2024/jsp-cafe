package org.example.dto;

import org.example.entity.Article;

public record ArticleCreateReqDto(
    String author,
    String title,
    String content
) {
    public static ArticleCreateReqDto of(String author, String title, String content) {
        return new ArticleCreateReqDto(author, title, content);
    }

    public Article toEntity() {
        return new Article(title, content, author);
    }
}
