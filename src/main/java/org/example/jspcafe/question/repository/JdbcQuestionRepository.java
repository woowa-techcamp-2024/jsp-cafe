package org.example.jspcafe.question.repository;

import org.example.jspcafe.database.SimpleConnectionPool;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcQuestionRepository implements QuestionRepository {

    @Override
    public Long save(Question question) {
        String sql = "INSERT INTO Question (user_id, title, contents, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = SimpleConnectionPool.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, question.getUserId());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(question.getLastModifiedDate()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating question failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM Question ORDER BY date DESC";
        try (Connection conn = SimpleConnectionPool.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Question question = Question.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("user_id"))
                        .title(rs.getString("title"))
                        .contents(rs.getString("contents"))
                        .lastModifiedDate(rs.getTimestamp("date").toLocalDateTime())
                        .build();
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    @Override
    public Optional<Question> findById(Long id) {
        String sql = "SELECT " +
                "q.id AS question_id, q.title AS question_title, q.contents AS question_contents, q.date AS question_date, " +
                "u.id AS user_id, u.user_id AS user_user_id, u.nickname AS user_nickname, u.email AS user_email " +
                "FROM Question q " +
                "JOIN Users u ON q.user_id = u.id " +
                "WHERE q.id = ?";

        try (Connection conn = SimpleConnectionPool.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .userId(rs.getString("user_user_id"))
                            .email(rs.getString("user_email"))
                            .nickname(rs.getString("user_nickname"))
                            .id(rs.getLong("user_id"))
                            .build();

                    Question question = Question.builder()
                            .title(rs.getString("question_title"))
                            .id(rs.getLong("question_id"))
                            .userId(rs.getLong("user_id"))
                            .lastModifiedDate(rs.getTimestamp("question_date").toLocalDateTime())
                            .contents(rs.getString("question_contents"))
                            .user(user)
                            .build();

                    return Optional.of(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}