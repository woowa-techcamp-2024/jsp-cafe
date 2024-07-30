package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Reply;

import java.util.Map;

public record SaveReplyDto(Long articleId, String contents) {

    public static SaveReplyDto from(final Map<String, String> request) {
        return new SaveReplyDto(
                Long.parseLong(request.get("articleId")),
                request.get("contents")
        );
    }

    public Reply toEntity(final String writerId) {
        return new Reply(articleId, writerId, contents());
    }
}
