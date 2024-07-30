package org.example.cafe.application.dto;

import org.example.cafe.domain.Question;

public record QuestionUpdateDto(Long questionId,
                                String title,
                                String contents) {

    public Question toQuestion(String writer) {
        return new Question(questionId, title, contents, writer);
    }
}
