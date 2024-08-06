package org.example.cafe.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.infrastructure.jdbc.GeneratedKeyHolder;
import org.example.cafe.infrastructure.jdbc.JdbcTemplate;
import org.example.cafe.infrastructure.jdbc.RowMapper;

public class ReplyJdbcRepository implements ReplyRepository {

    private static final String INSERT = "INSERT INTO REPLY (content, writer, question_id) VALUES (?, ?, ?)";
    private static final String SELECT_BY_QUESTION_ID = "SELECT * FROM REPLY WHERE question_id = ? and is_deleted = false";
    private static final String SELECT_BY_ID = "SELECT * FROM REPLY WHERE reply_id = ? and is_deleted = false";
    private static final String DELETE = "DELETE FROM REPLY";
    private static final String UPDATE_BY_ID = "UPDATE REPLY SET content = ?, writer = ?, question_id = ?, is_deleted = ? WHERE reply_id = ?";

    private static final ReplyRowMapper replyRowMapper = new ReplyRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public ReplyJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Reply reply) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT, keyHolder, reply.getContent(), reply.getWriter(), reply.getQuestionId());

        return keyHolder.getKey();
    }

    @Override
    public Reply findById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, replyRowMapper, id);
    }

    @Override
    public List<Reply> findByQuestionId(Long questionId) {
        return jdbcTemplate.query(SELECT_BY_QUESTION_ID, replyRowMapper, questionId);
    }

    @Override
    public void update(Reply reply) {
        jdbcTemplate.update(UPDATE_BY_ID, null,
                reply.getContent(), reply.getWriter(), reply.getQuestionId(), reply.getIsDeleted(), reply.getReplyId());
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE, null);
    }

    static class ReplyRowMapper implements RowMapper<Reply> {

        @Override
        public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReplyBuilder()
                    .replyId(rs.getLong("reply_id"))
                    .writer(rs.getString("writer"))
                    .content(rs.getString("content"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .questionId(rs.getLong("question_id"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime()).build();
        }
    }
}
