package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.QuestionRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JdbcQuestionRepository implements QuestionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcQuestionRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS question\n"
                        + "(\n"
                        + "    id           BIGINT AUTO_INCREMENT PRIMARY KEY,\n"
                        + "    writer       VARCHAR(30),\n"
                        + "    title        TEXT,\n"
                        + "    contents     TEXT,\n"
                        + "    created_time DATETIME,\n"
                        + "    users_primary_id BIGINT,\n"
                        + "    FOREIGN KEY (users_primary_id) REFERENCES users(id)\n"
                        + ")");
    }

    @Override
    public void save(final Question question) {
        String query = "INSERT INTO question(writer, title, contents, created_time, users_primary_id) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                query,
                ps -> {
                    ps.setString(1, question.getWriter());
                    ps.setString(2, question.getTitle());
                    ps.setString(3, question.getContents());
                    ps.setTimestamp(4, Timestamp.valueOf(question.getCreatedTime()));
                    ps.setLong(5, question.getUserPrimaryId());
                }
        );
    }

    @Override
    public List<Question> findAllOrderByCreatedTimeDesc() {
        String query = "SELECT id, writer, title, contents, created_time, users_primary_id FROM question ORDER BY created_time DESC";

        return jdbcTemplate.selectAll(
                query,
                resultSet -> new Question(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5).toLocalDateTime(),
                        resultSet.getLong(6)
                )
        );
    }

    @Override
    public Optional<Question> findById(final Long id) {
        String query = "SELECT id, writer, title, contents, created_time, users_primary_id FROM question WHERE id = ?";

        Question question = jdbcTemplate.selectOne(
                query,
                ps -> ps.setLong(1, id),
                resultSet -> new Question(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5).toLocalDateTime(),
                        resultSet.getLong(6)
                )
        );

        return Optional.ofNullable(question);
    }
}
