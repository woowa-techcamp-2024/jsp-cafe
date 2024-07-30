package org.example.cafe.application.dto;


import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;

public record ReplyCreateDto(String content,
                             Long questionId) {

    public Reply toReply(String writer) {
        return new ReplyBuilder()
                .writer(writer)
                .content(content)
                .questionId(questionId).build();
    }
}
