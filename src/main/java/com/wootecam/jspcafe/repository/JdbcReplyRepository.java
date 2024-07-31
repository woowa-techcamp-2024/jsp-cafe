package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.ReplyRepository;

public class JdbcReplyRepository implements ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReplyRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS reply\n"
                        + "(\n"
                        + "    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                        + "    writer VARCHAR(255) NOT NULL,\n"
                        + "    contents TEXT NOT NULL,\n"
                        + "    created_time DATETIME NOT NULL,\n"
                        + "    users_primary_id BIGINT NOT NULL,\n"
                        + "    question_primary_id BIGINT NOT NULL,\n"
                        + "    FOREIGN KEY (users_primary_id) REFERENCES users(id),\n"
                        + "    FOREIGN KEY (question_primary_id) REFERENCES question(id)\n"
                        + ")");
    }
}
