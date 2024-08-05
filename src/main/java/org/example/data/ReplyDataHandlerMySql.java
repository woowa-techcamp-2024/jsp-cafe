package org.example.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.example.constance.AliveStatus;
import org.example.domain.Reply;

public class ReplyDataHandlerMySql implements ReplyDataHandler {
    @Override
    public Reply insert(Reply reply) {
        String sql = "INSERT INTO replies (user_id, article_id, author, comment, alive_status, created_dt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, reply.getUserId());
            pstmt.setLong(2, reply.getArticleId());
            pstmt.setString(3, reply.getAuthor());
            pstmt.setString(4, reply.getComment());
            pstmt.setString(5, reply.getAliveStatus().name());
            pstmt.setTimestamp(6, Timestamp.valueOf(reply.getCreatedDt()));
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    reply = new Reply(id, reply.getUserId(), reply.getArticleId(), reply.getAuthor(),
                            reply.getComment(), reply.getAliveStatus(), reply.getCreatedDt());
                } else {
                    throw new SQLException("Creating reply failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert reply", e);
        }
        return reply;
    }

    @Override
    public Reply update(Reply reply) {
        String sql = "UPDATE replies SET comment = ?, alive_status = ? where replies.reply_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql)) {
            pstmt.setString(1, reply.getComment());
            pstmt.setString(2, reply.getAliveStatus().name());
            pstmt.setLong(3, reply.getReplyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update reply", e);
        }
        return reply;
    }

    @Override
    public Reply findByReplyId(Long replyId) {
        String sql = "SELECT * FROM replies WHERE reply_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, replyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Reply(rs.getLong("reply_id"), rs.getLong("user_id"), rs.getLong("article_id"),
                            rs.getString("author"), rs.getString("comment"),
                            AliveStatus.valueOf(rs.getString("alive_status")),
                            rs.getTimestamp("created_dt").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find reply by ID", e);
        }
        return null;
    }

    @Override
    public List<Reply> findAllByArticleId(Long articleId) {
        String sql = "SELECT * FROM replies where replies.alive_status = ? and replies.article_id = ? ORDER BY created_dt DESC";
        List<Reply> replies = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql)) {
            pstmt.setString(1, AliveStatus.ALIVE.name());
            pstmt.setLong(2, articleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                replies.add(
                        new Reply(rs.getLong("reply_id"), rs.getLong("user_id"), rs.getLong("article_id"),
                                rs.getString("author"), rs.getString("comment"),
                                AliveStatus.valueOf(rs.getString("alive_status")),
                                rs.getTimestamp("created_dt").toLocalDateTime())
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all replies", e);
        }
        return replies;
    }

    @Override
    public void deleteAllByArticleId(Long articleId) {
        String sql = "UPDATE replies SET alive_status = ? where replies.article_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql)) {
            pstmt.setString(1, AliveStatus.DELETED.name());
            pstmt.setLong(2, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update reply", e);
        }
    }
}
