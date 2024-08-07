package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.Question;
import java.time.LocalDateTime;

public class QuestionResponse {

    private final Long id;
    private final String writer;
    private final String title;
    private final LocalDateTime createdTime;

    public QuestionResponse(final Long id, final String writer, final String title,
                            final LocalDateTime createdTime) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdTime = createdTime;
    }

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getWriter(),
                question.getTitle(),
                question.getCreatedTime()
        );
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}
