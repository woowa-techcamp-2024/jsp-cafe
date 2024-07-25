package org.example.jspcafe.question.service;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.repository.QuestionRepository;

import static org.example.jspcafe.common.DateTimeUtil.getCurrentDateTimeString;

public class QuestionService {
    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long saveQuestion(Question question) {
        question.setDate(getCurrentDateTimeString());
        return questionRepository.save(question);
    }
}
