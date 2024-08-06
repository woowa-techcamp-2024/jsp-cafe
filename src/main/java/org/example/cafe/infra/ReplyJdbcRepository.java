package org.example.cafe.infra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.cafe.application.ReplyService.ReplyPageDto;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.infra.jdbc.GeneratedKeyHolder;
import org.example.cafe.infra.jdbc.JdbcTemplate;
import org.example.cafe.infra.jdbc.RowMapper;

public class ReplyJdbcRepository implements ReplyRepository {

    private static final String INSERT = "INSERT INTO REPLY (content, writer, question_id) VALUES (?, ?, ?)";
    private static final String SELECT_FIRST_PAGE_BY_QUESTION_ID = """
            SELECT * FROM REPLY
                     WHERE question_id = ? and is_deleted = false
            ORDER BY created_at, reply_id
            limit ?;
            """;
    private static final String SELECT_NEXT_PAGE_BY_QUESTION_ID = """
            SELECT * FROM REPLY
                     WHERE question_id = ? and is_deleted = false
                     and (created_at > ? or (created_at = ? and reply_id > ?))
            ORDER BY created_at, reply_id
            limit ?;
            """;
    private static final String SELECT_BY_QUESTION_ID = "SELECT * FROM REPLY WHERE question_id = ? and is_deleted = false";
    private static final String SELECT_BY_ID = "SELECT * FROM REPLY WHERE reply_id = ? and is_deleted = false";
    private static final String DELETE = "DELETE FROM REPLY";
    private static final String UPDATE_BY_ID = "UPDATE REPLY SET content = ?, writer = ?, question_id = ?, is_deleted = ? WHERE reply_id = ?";
    private static final String UPDATE_IS_DELETED_TRUE_BY_QUESION_ID = "UPDATE REPLY SET is_deleted = true WHERE question_id = ?";

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
    public List<Reply> findByQuestionId(ReplyPageDto replyPageDto) {
        if (replyPageDto.lastReplyId() == null || replyPageDto.createdAt() == null) {
            return jdbcTemplate.query(SELECT_FIRST_PAGE_BY_QUESTION_ID, replyRowMapper,
                    replyPageDto.questionId(), replyPageDto.pageSize());
        }

        return jdbcTemplate.query(SELECT_NEXT_PAGE_BY_QUESTION_ID, replyRowMapper,
                replyPageDto.questionId(), replyPageDto.createdAt(), replyPageDto.createdAt(),
                replyPageDto.lastReplyId(), replyPageDto.pageSize());
    }

    @Override
    public void update(Reply reply) {
        jdbcTemplate.update(UPDATE_BY_ID, null,
                reply.getContent(), reply.getWriter(), reply.getQuestionId(), reply.getIsDeleted(), reply.getReplyId());
    }

    @Override
    public void deleteByQuestionId(Long questionId) {
        jdbcTemplate.update(UPDATE_IS_DELETED_TRUE_BY_QUESION_ID, null, questionId);
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
