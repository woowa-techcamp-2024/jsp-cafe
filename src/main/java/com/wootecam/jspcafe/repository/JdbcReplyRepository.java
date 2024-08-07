package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.config.KeyHolder;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.ReplyRepository;
import java.sql.Timestamp;
import java.util.List;
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
        String query = "SELECT id, writer, contents, created_time, users_primary_id, question_primary_id FROM reply WHERE deleted_at IS NULL AND id = ?";

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

    @Override
    public List<Reply> findAllByQuestionPrimaryIdLimit(final Long questionPrimaryId, final int count) {
        String query = "SELECT id, writer, contents, created_time, users_primary_id, question_primary_id FROM reply WHERE deleted_at IS NULL AND question_primary_id = ? LIMIT ?";

        return jdbcTemplate.selectAll(
                query,
                ps -> {
                    ps.setLong(1, questionPrimaryId);
                    ps.setInt(2, count);
                },
                resultSet -> new Reply(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime(),
                        resultSet.getLong(5),
                        resultSet.getLong(6)
                )
        );
    }

    @Override
    public void delete(final Long id) {
        String query = "UPDATE reply SET deleted_at = now() WHERE id = ?";

        jdbcTemplate.update(
                query
                , ps -> ps.setLong(1, id)
        );
    }

    @Override
    public boolean existsReplyByIdAndOtherUserPrimaryId(final Long id, final Long userPrimaryId) {
        String query = "SELECT EXISTS(SELECT 1 FROM reply WHERE reply.deleted_at IS NULL AND question_primary_id = ? AND users_primary_id != ?)";

        return jdbcTemplate.selectOne(
                query,
                ps -> {
                    ps.setLong(1, id);
                    ps.setLong(2, userPrimaryId);
                },
                resultSet -> resultSet.getBoolean(1)
        );
    }

    @Override
    public void deleteAllByQuestionPrimaryId(final Long questionPrimaryId) {
        String query = "UPDATE reply SET deleted_at = now() WHERE deleted_at IS NULL AND question_primary_id = ?";

        jdbcTemplate.update(
                query,
                ps -> ps.setLong(1, questionPrimaryId)
        );
    }

    @Override
    public int countAll(final Long questionPrimaryId) {
        String query = "SELECT COUNT(*) FROM reply WHERE deleted_at IS NULL AND question_primary_id = ?";

        return jdbcTemplate.selectOne(
                query,
                ps -> ps.setLong(1, questionPrimaryId),
                resultSet -> resultSet.getInt(1)
        );
    }

    @Override
    public List<Reply> findAllByQuestionPrimaryIdAndStartWith(final Long questionPrimaryId, final Long lastReplyId,
                                                              final int count) {
        String query =
                "SELECT r.id, writer, contents, created_time, users_primary_id, question_primary_id FROM reply r JOIN ("
                        + " SELECT id"
                        + " FROM reply r"
                        + " WHERE deleted_at IS NULL AND question_primary_id = ? AND id > ?"
                        + " LIMIT ?"
                        + " ) temp ON r.id = temp.id";

        return jdbcTemplate.selectAll(
                query,
                ps -> {
                    ps.setLong(1, questionPrimaryId);
                    ps.setLong(2, lastReplyId);
                    ps.setInt(3, count);
                },
                resultSet -> new Reply(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toLocalDateTime(),
                        resultSet.getLong(5),
                        resultSet.getLong(6)
                )
        );
    }
}
