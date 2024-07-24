package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.model.Question;
import com.wootecam.jspcafe.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void append(final String writer, final String title, final String contents) {
        Long id = questionRepository.generateId();
        Question question = new Question(id, writer, title, contents);

        log.info("write question = {}", question);

        questionRepository.save(question);
    }
}
