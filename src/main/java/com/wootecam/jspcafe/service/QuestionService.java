package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.QuestionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void append(final String writer, final String title, final String contents) {
        Question question = new Question(writer, title, contents, LocalDateTime.now());

        log.info("write question = {}", question);

        questionRepository.save(question);
    }

    public List<Question> readAll() {
        return questionRepository.findAll();
    }

    public Question read(final Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다. id = " + id));
    }
}
