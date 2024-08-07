package com.woowa.cafe.dto.article;

public record ArticleQueryDto(Long articleId, String title, String updatedAt,
                              String writerId, Long replyCount) {
}
