package org.example.cafe.application.dto;

import org.example.cafe.domain.Question;

public record QuestionCreateDto(String title,
                                String content,
                                String writer) {

    public Question toQuestion() {
        return new Question(title, content, writer);
    }
}
