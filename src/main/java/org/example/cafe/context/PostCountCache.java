package org.example.cafe.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.infrastructure.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostCountCache {

    private static final Logger log = LoggerFactory.getLogger(PostCountCache.class);
    private final QuestionRepository questionRepository;
    private ScheduledExecutorService scheduler;
    private volatile Long postCount = 0L;

    public PostCountCache(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.scheduler = Executors.newScheduledThreadPool(1);

        updatePostCount();
        scheduler.scheduleAtFixedRate(this::updatePostCount, 10, 10, TimeUnit.SECONDS);
    }

    public Long getPostCount() {
        return postCount;
    }

    private void updatePostCount() {
        try {
            postCount = questionRepository.count(null);
            log.debug("Post count updated: {}", postCount);
        } catch (Exception e) {
            log.error("Failed to update post count", e);
        }
    }

    public void close() {
        scheduler.shutdown();
    }

    static class LongRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }
}
