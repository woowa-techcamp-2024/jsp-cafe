package com.woowa.cafe.dto.article;

import java.util.List;

public record ArticlePageDto(List<ArticleListDto> articles, int currentPage, int totalCount, boolean nextPages) {
    public static ArticlePageDto of(final List<ArticleListDto> articles, final int currentPage,
                                    final int totalCount, final boolean nextPages) {
        return new ArticlePageDto(articles, currentPage, totalCount, nextPages);
    }
}
