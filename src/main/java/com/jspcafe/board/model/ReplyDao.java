package com.jspcafe.board.model;

import com.jspcafe.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReplyDao {
    private final DatabaseConnector databaseConnector;

    public ReplyDao(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void save(final Reply reply) {
        String sql = "INSERT INTO replies (id, article_id, user_id, nickname, content, create_at, update_at, is_deleted, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reply.id());
            pstmt.setString(2, reply.articleId());
            pstmt.setString(3, reply.userId());
            pstmt.setString(4, reply.nickname());
            pstmt.setString(5, reply.content());
            pstmt.setTimestamp(6, Timestamp.valueOf(reply.createAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(reply.updateAt()));
            pstmt.setBoolean(8, false);
            pstmt.setNull(9, Types.TIMESTAMP);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving reply", e);
        }
    }

    public Optional<Reply> findById(final String id) {
        String sql = "SELECT * FROM replies WHERE id = ? AND is_deleted = false";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createReplyFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reply by id", e);
        }
        return Optional.empty();
    }

    public List<Reply> findByArticleId(final String articleId) {
        String sql = "SELECT * FROM replies WHERE article_id = ? AND is_deleted = false ORDER BY create_at ASC";
        List<Reply> replies = new ArrayList<>();
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    replies.add(createReplyFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding replies by article id", e);
        }
        return replies;
    }

    public void update(final Reply updateReply) {
        String sql = "UPDATE replies SET content = ?, update_at = ? WHERE id = ? AND is_deleted = false";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updateReply.content());
            pstmt.setTimestamp(2, Timestamp.valueOf(updateReply.updateAt()));
            pstmt.setString(3, updateReply.id());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating reply failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating reply", e);
        }
    }

    public void softDelete(final String id) {
        String sql = "UPDATE replies SET is_deleted = true, deleted_at = ? WHERE id = ?";
        try(Connection conn = databaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Soft deleting reply failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error soft deleting reply", e);
        }
    }

    private Reply createReplyFromResultSet(ResultSet rs) throws SQLException {
        return new Reply(
                rs.getString("id"),
                rs.getString("article_id"),
                rs.getString("user_id"),
                rs.getString("nickname"),
                rs.getString("content"),
                rs.getTimestamp("create_at").toLocalDateTime(),
                rs.getTimestamp("update_at").toLocalDateTime()
        );
    }
}
