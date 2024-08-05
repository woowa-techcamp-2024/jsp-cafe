package org.example.cafe.context;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.example.cafe.domain.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionCountCache {

    private static final Logger log = LoggerFactory.getLogger(QuestionCountCache.class);
    private final QuestionRepository questionRepository;
    private ScheduledExecutorService scheduler;
    private volatile Long postCount = 0L;

    public QuestionCountCache(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);

        updateQuestionCount();
        scheduler.scheduleAtFixedRate(this::updateQuestionCount, 10, 10, TimeUnit.SECONDS);
    }

    public Long getPostCount() {
        return postCount;
    }

    private void updateQuestionCount() {
        try {
            postCount = questionRepository.count(null);
            log.debug("Question count updated: {}", postCount);
        } catch (Exception e) {
            log.error("Failed to update question count", e);
        }
    }

    public void close() {
        scheduler.shutdown();
    }
}
