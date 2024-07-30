package org.example.cafe.application.dto;


import org.example.cafe.domain.Reply;

public record ReplyCreateDto(String content,
                             Long questionId) {

    public Reply toReply(String writer) {
        return new Reply(writer, content, questionId);
    }
}
