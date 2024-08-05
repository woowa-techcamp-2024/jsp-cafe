package org.example.jspcafe.reply.repository;

import org.example.jspcafe.database.SimpleConnectionPool;
import org.example.jspcafe.reply.Reply;
import org.example.jspcafe.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReplyRepository implements ReplyRepository {

    @Override
    public Long save(Reply reply) {
        String sql = "INSERT INTO Reply (user_id, question_id, contents, date) VALUES (?, ?, ?, ?)";

        Connection conn = SimpleConnectionPool.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, reply.getUserId());
            pstmt.setLong(2, reply.getQuestionId());
            pstmt.setString(3, reply.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(reply.getLastModifiedDate()));

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
    public List<Reply> getAllByQuestionId(Long questionId) {

        List<Reply> replies = new ArrayList<>();

        Connection conn = SimpleConnectionPool.getInstance().getConnection();

        String sql = "SELECT " +
                "r.id AS reply_id, r.contents AS reply_contents, r.date AS reply_date, " +
                "u.id AS user_id, u.user_id AS user_user_id, u.nickname AS user_nickname, u.email AS user_email " +
                "FROM Reply r " +
                "JOIN Users u ON r.user_id = u.id " +
                "WHERE r.question_id = ? ORDER BY r.date DESC"; // ORDER BY로 수정

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, questionId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getLong("user_id"))
                        .nickname(rs.getString("user_nickname"))
                        .build();

                replies.add(Reply.builder()
                        .user(user)
                        .id(rs.getLong("reply_id"))
                        .userId(rs.getLong("user_id"))
                        .contents(rs.getString("reply_contents"))
                        .lastModifiedDate(rs.getTimestamp("reply_date").toLocalDateTime())
                        .build());
            }

            return replies;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return List.of();
    }


    @Override
    public Optional<Reply> findById(Long id) {
        String sql = "SELECT " +
                "r.id AS reply_id, r.contents AS reply_contents, r.date AS reply_date, " +
                "u.id AS user_id, u.user_id AS user_user_id, u.nickname AS user_nickname, u.email AS user_email " +
                "FROM Reply r " +
                "JOIN Users u ON r.user_id = u.id " +
                "WHERE r.id = ?";

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

                    Reply reply = Reply.builder()
                            .id(rs.getLong("reply_id"))
                            .userId(rs.getLong("user_id"))
                            .lastModifiedDate(rs.getTimestamp("reply_date").toLocalDateTime())
                            .contents(rs.getString("reply_contents"))
                            .user(user)
                            .build();

                    return Optional.of(reply);
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
    public void deleteById(Long replyId) {
        String sql = "DELETE FROM Reply WHERE id = ?";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, replyId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting reply failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
