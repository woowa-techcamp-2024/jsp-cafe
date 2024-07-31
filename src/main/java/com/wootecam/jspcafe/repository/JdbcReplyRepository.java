package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.config.KeyHolder;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.ReplyRepository;
import java.sql.Timestamp;
import java.util.Optional;

public class JdbcReplyRepository implements ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReplyRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS reply\n"
                        + "(\n"
                        + "    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                        + "    writer              VARCHAR(255) NOT NULL,\n"
                        + "    contents            TEXT         NOT NULL,\n"
                        + "    created_time        DATETIME     NOT NULL,\n"
                        + "    deleted_at          DATETIME,\n"
                        + "    users_primary_id    BIGINT       NOT NULL,\n"
                        + "    question_primary_id BIGINT       NOT NULL,\n"
                        + "    FOREIGN KEY (users_primary_id) REFERENCES users (id),\n"
                        + "    FOREIGN KEY (question_primary_id) REFERENCES question (id)\n"
                        + ")"
        );
    }

    @Override
    public Long save(final Reply reply) {
        String query = "INSERT INTO reply(writer, contents, created_time, users_primary_id, question_primary_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(
                query,
                ps -> {
                    ps.setString(1, reply.getWriter());
                    ps.setString(2, reply.getContents());
                    ps.setTimestamp(3, Timestamp.valueOf(reply.getCreatedTime()));
                    ps.setLong(4, reply.getUserPrimaryId());
                    ps.setLong(5, reply.getQuestionPrimaryId());
                },
                keyHolder
        );

        return keyHolder.getId();
    }

    @Override
    public Optional<Reply> findById(final Long id) {
        String query = "SELECT id, writer, contents, created_time, users_primary_id, question_primary_id FROM reply WHERE deleted_at IS NULL and id = ?";

        Reply reply = jdbcTemplate.selectOne(
                query,
                ps -> ps.setLong(1, id),
                resultSet -> new Reply(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime(),
                        resultSet.getLong(5),
                        resultSet.getLong(6)
                )
        );

        return Optional.ofNullable(reply);
    }
}
