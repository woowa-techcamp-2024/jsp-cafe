package org.example.cafe.context;

import static org.example.cafe.application.QuestionService.PAGE_SIZE;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.example.cafe.domain.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionCountCache {

    private static final Logger log = LoggerFactory.getLogger(QuestionCountCache.class);
    private final QuestionRepository questionRepository;
    private final ScheduledExecutorService scheduler;
    private volatile Long questionCount;
    private volatile Long questionTotalPage;

    public QuestionCountCache(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);

        updateQuestionCount();
        scheduler.scheduleAtFixedRate(this::updateQuestionCount, 10, 10, TimeUnit.SECONDS);
    }

    public Long getQuestionCount() {
        return questionCount;
    }

    public Long getQuestionTotalPage() {
        return questionTotalPage;
    }

    private void updateQuestionCount() {
        try {
            questionCount = questionRepository.count(null);
            questionTotalPage = (questionCount / PAGE_SIZE) + 1;
            log.debug("Question count updated: {}", questionCount);
        } catch (Exception e) {
            log.error("Failed to update question count", e);
        }
    }

    public void close() {
        scheduler.shutdown();
    }
}
