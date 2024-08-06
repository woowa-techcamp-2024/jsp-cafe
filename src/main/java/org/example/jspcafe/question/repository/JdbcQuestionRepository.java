package org.example.jspcafe.question.repository;

import org.example.jspcafe.database.SimpleConnectionPool;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.QuestionPagination;
import org.example.jspcafe.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcQuestionRepository implements QuestionRepository {

    @Override
    public Long save(Question question) {
        String sql = "INSERT INTO Question (user_id, title, contents, date, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, question.getUserId());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(question.getLastModifiedDate()));
            pstmt.setBoolean(5, true);

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
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, u.id as user_id, u.nickname, u.email " +
                "FROM Question q " +
                "JOIN Users u ON q.user_id = u.id " +
                "WHERE q.status = true " +
                "ORDER BY q.date DESC";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getLong("user_id"))
                        .nickname(rs.getString("nickname"))
                        .email(rs.getString("email"))
                        .build();

                Question question = Question.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("user_id"))
                        .user(user)
                        .title(rs.getString("title"))
                        .contents(rs.getString("contents"))
                        .lastModifiedDate(rs.getTimestamp("date").toLocalDateTime())
                        .build();
                questions.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return questions;
    }


    @Override
    public Optional<Question> findById(Long id) {
        String sql = "SELECT " +
                "q.id AS question_id, q.title AS question_title, q.contents AS question_contents, q.date AS question_date, q.status AS question_status, " +
                "u.id AS user_id, u.user_id AS user_user_id, u.nickname AS user_nickname, u.email AS user_email " +
                "FROM Question q " +
                "JOIN Users u ON q.user_id = u.id " +
                "WHERE q.id = ? AND q.status = true";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Question question) {
        String sql = "UPDATE Question SET title = ?, contents = ?, date = ? WHERE id = ? AND status = true";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getContents());
            pstmt.setTimestamp(3, Timestamp.valueOf(question.getLastModifiedDate()));
            pstmt.setLong(4, question.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE Question SET status = false WHERE id = ?";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
    }

    public QuestionPagination getAllWithPagination(int page, int pageSize) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.*, u.id as user_id, u.nickname, u.email " +
                "FROM Question q " +
                "JOIN Users u ON q.user_id = u.id " +
                "WHERE q.status = true " +
                "ORDER BY q.date DESC " +
                "LIMIT ? OFFSET ?";

        Connection conn = SimpleConnectionPool.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = User.builder()
                            .id(rs.getLong("user_id"))
                            .nickname(rs.getString("nickname"))
                            .email(rs.getString("email"))
                            .build();

                    Question question = Question.builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getLong("user_id"))
                            .user(user)
                            .title(rs.getString("title"))
                            .contents(rs.getString("contents"))
                            .lastModifiedDate(rs.getTimestamp("date").toLocalDateTime())
                            .build();
                    questions.add(question);
                }
            }

            // 전체 질문 수를 계산하기 위한 쿼리
            String countSql = "SELECT COUNT(*) FROM Question WHERE status = true";
            try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                 ResultSet countRs = countStmt.executeQuery()) {
                if (countRs.next()) {
                    long totalItems = countRs.getLong(1);
                    return new QuestionPagination(questions, page, pageSize, totalItems);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return new QuestionPagination(questions, page, pageSize, 0);
    }

}
