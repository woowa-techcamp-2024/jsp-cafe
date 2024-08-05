package org.example.cafe.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.infrastructure.jdbc.GeneratedKeyHolder;
import org.example.cafe.infrastructure.jdbc.JdbcTemplate;
import org.example.cafe.infrastructure.jdbc.RowMapper;

public class QuestionJdbcRepository implements QuestionRepository {

    private static final String INSERT = "INSERT INTO QUESTION (title, content, writer) VALUES (?, ?, ?)";
    private static final String SELECT = "SELECT * FROM QUESTION WHERE is_deleted = false";
    private static final String SELECT_BY_ID = "SELECT * FROM QUESTION WHERE question_id = ? and is_deleted = false";
    private static final String DELETE = "DELETE FROM QUESTION";
    private static final String DELETE_BY_ID = "DELETE FROM QUESTION WHERE question_id = ?";
    private static final String UPDATE_BY_ID = "UPDATE QUESTION SET title = ?, content = ?, writer = ?, is_deleted = ? WHERE question_id = ?";

    private static final QuestionRowMapper questionRowMapper = new QuestionRowMapper();
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

    public List<Question> findAll() {
        return jdbcTemplate.query(SELECT, questionRowMapper);
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
}
