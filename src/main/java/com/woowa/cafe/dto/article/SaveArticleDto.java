package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Article;

import java.util.Map;

public record SaveArticleDto(String title, String content) {

    public static SaveArticleDto from(final Map<String, String> body) {
        return new SaveArticleDto(body.get("title"),
                body.get("contents"));
    }

    public Article toEntity(final String writerId) {
        return new Article(title(), content(), writerId);
    }
}
