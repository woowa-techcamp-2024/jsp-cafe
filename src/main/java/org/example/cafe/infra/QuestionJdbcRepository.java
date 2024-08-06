package org.example.cafe.infra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.infra.jdbc.GeneratedKeyHolder;
import org.example.cafe.infra.jdbc.JdbcTemplate;
import org.example.cafe.infra.jdbc.RowMapper;

public class QuestionJdbcRepository implements QuestionRepository {

    private static final String INSERT = "INSERT INTO QUESTION (title, content, writer) VALUES (?, ?, ?)";
    private static final String SELECT = """
            select * from Question as q1 JOIN (
                select question_id from QUESTION
                WHERE is_deleted = false
                ORDER BY created_at DESC 
                LIMIT ?, ?) as q2
                on q1.question_id = q2.question_id;
            """;
    private static final String SELECT_BY_ID = "SELECT * FROM QUESTION WHERE question_id = ? and is_deleted = false";
    private static final String COUNT = "SELECT COUNT(question_id) FROM QUESTION WHERE is_deleted = false;";
    private static final String COUNT_BY_KEYWORD = "SELECT COUNT(question_id) FROM QUESTION WHERE is_deleted = false and (title like %?% or content like %?%)";
    private static final String DELETE = "DELETE FROM QUESTION";
    private static final String DELETE_BY_ID = "DELETE FROM QUESTION WHERE question_id = ?";
    private static final String UPDATE_BY_ID = "UPDATE QUESTION SET title = ?, content = ?, writer = ?, is_deleted = ? WHERE question_id = ?";

    private static final QuestionRowMapper questionRowMapper = new QuestionRowMapper();
    private static final LongRowMapper longRowMapper = new LongRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public QuestionJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Question question) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT, keyHolder, question.getTitle(), question.getContent(), question.getWriter());

        return keyHolder.getKey();
    }

    @Override
    public Question findById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, questionRowMapper, id);
    }

    public List<Question> findAll(Long page, int pageSize) {
        long offset = (page - 1) * pageSize;

        return jdbcTemplate.query(SELECT, questionRowMapper, offset, pageSize);
    }

    @Override
    public Long count(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return jdbcTemplate.queryForObject(COUNT, longRowMapper);
        }

        return jdbcTemplate.queryForObject(COUNT_BY_KEYWORD, longRowMapper, keyword, keyword);
    }

    @Override
    public void update(Question question) {
        jdbcTemplate.update(UPDATE_BY_ID, null,
                question.getTitle(), question.getContent(), question.getWriter(), question.isDeleted(),
                question.getQuestionId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, null, id);
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE, null);
    }

    static class QuestionRowMapper implements RowMapper<Question> {

        @Override
        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Question(
                    rs.getLong("question_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("writer"),
                    rs.getBoolean("is_deleted"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }

    static class LongRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }
}
