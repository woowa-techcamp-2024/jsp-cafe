package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.DatabaseManager;
import org.example.entity.Reply;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

public class ReplyRepositoryDBImpl implements ReplyRepository {

    private static ReplyRepositoryDBImpl instance;
    private final Logger logger = LoggerUtil.getLogger();


    @Override
    public void save(Reply reply)
    {
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO replies (article_id, content, author) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, reply.getArticleId());
            pstmt.setString(2, reply.getContent());
            pstmt.setString(3, reply.getAuthorId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save reply", e);
        }
    }


    @Override
    public void deleteById(int replyId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE replies SET deleted = TRUE WHERE reply_id = ?")) {
            pstmt.setInt(1, replyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete reply", e);
        }
    }

    @Override
    public List<Reply> findAllByArticleId(int articleId, int lastReplyId, int count) {
        List<Reply> replies = new ArrayList<>();
        String query;
        if (lastReplyId == -1) {
            query = "SELECT * FROM replies WHERE article_id = ? AND deleted = FALSE ORDER BY reply_id DESC LIMIT ?";
        } else {
            query = "SELECT * FROM replies WHERE article_id = ? AND deleted = FALSE AND reply_id < ? ORDER BY reply_id DESC LIMIT ?";
        }

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, articleId);
            if (lastReplyId == -1) {
                pstmt.setInt(2, count);
            } else {
                pstmt.setInt(2, lastReplyId);
                pstmt.setInt(3, count);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reply reply = new Reply(
                        rs.getInt("reply_id"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getInt("article_id"),
                        rs.getBoolean("deleted")
                    );
                    replies.add(reply); // 리스트에 Reply 객체 추가
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find all replies by article id", e);
        }
        return replies;
    }


    @Override
    public void deleteAllByArticleId(int articleId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE replies SET deleted = TRUE WHERE article_id = ?")) {
            pstmt.setInt(1, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete all replies by article id", e);
        }
    }

    @Override
    public Optional<Reply> findById(int replyId) {
        String query = "SELECT * FROM replies WHERE reply_id = ? AND deleted = FALSE";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, replyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Reply(
                        rs.getInt("reply_id"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getInt("article_id"),
                        rs.getBoolean("deleted")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Failed to find reply by id", e);
        }
        return null;
    }

    private ReplyRepositoryDBImpl() {
        // Private constructor to prevent instantiation
    }

    public static ReplyRepository getInstance() {
        if (instance == null) {
            instance = new ReplyRepositoryDBImpl();
            instance.save(new Reply("content1", "test", 1));
            instance.save(new Reply("content1", "test", 1));
            instance.save(new Reply("content1", "test", 1));
            instance.save(new Reply("content1", "test", 1));
            instance.save(new Reply("content2", "test2", 2));
            instance.save(new Reply("content2", "test", 2));
        }
        return instance;
    }

    @Override
    public int findReplyCount(Integer articleId) {
        String query = "SELECT COUNT(*) FROM replies WHERE article_id = ? AND deleted = FALSE";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, articleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find reply count", e);
        }
        return 0;
    }

    @Override
    public List<Reply> findRealAll(int articleId) {
        List<Reply> replies = new ArrayList<>();
        String query = "SELECT * FROM replies WHERE article_id = ? AND deleted = FALSE";

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, articleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reply reply = new Reply(
                        rs.getInt("reply_id"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getInt("article_id"),
                        rs.getBoolean("deleted")
                    );
                    replies.add(reply); // 리스트에 Reply 객체 추가
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find all replies by article id", e);
        }
        return replies;
    }
}
