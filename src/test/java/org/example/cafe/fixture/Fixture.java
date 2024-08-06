package org.example.cafe.fixture;

import java.time.LocalDateTime;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;

public final class Fixture {

    private Fixture() {
    }

    public static Reply createReply(String content, String writer, Long questionId, String createdAt) {
        return new ReplyBuilder()
                .content(content)
                .writer(writer)
                .questionId(questionId)
                .createdAt(LocalDateTime.parse(createdAt))
                .build();
    }
}
