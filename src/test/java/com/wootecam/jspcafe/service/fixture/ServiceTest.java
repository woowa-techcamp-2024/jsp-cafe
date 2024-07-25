package com.wootecam.jspcafe.service.fixture;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.domain.UserRepository;
import com.wootecam.jspcafe.repository.JdbcQuestionRepository;
import com.wootecam.jspcafe.repository.JdbcUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ServiceTest {

    protected JdbcTemplate jdbcTemplate;

    protected UserRepository userRepository;

    protected QuestionRepository questionRepository;

    protected DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUpTest() {
        jdbcTemplate = new JdbcTemplate();
        databaseCleaner = new DatabaseCleaner(jdbcTemplate);
        userRepository = new JdbcUserRepository(jdbcTemplate);
        questionRepository = new JdbcQuestionRepository(jdbcTemplate);
    }

    @AfterEach
    void cleanDatabase() {
        databaseCleaner.clean();
    }
}
