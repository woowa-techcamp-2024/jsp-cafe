package org.example.jspcafe.question.service;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.repository.QuestionRepository;
import java.time.LocalDateTime;

public class QuestionService {
    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long saveQuestion(Question question) {
        question.setLastModifiedDate(LocalDateTime.now());
        return questionRepository.save(question);
    }
}
