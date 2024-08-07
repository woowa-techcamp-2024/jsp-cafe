package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Reply;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReplyRepository implements ReplyRepository {

    private final DataSource dataSource;

    public JdbcReplyRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long save(Reply reply) {
        String sql = "INSERT INTO replies (article_id, writer_id, contents, is_deleted, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, reply.getArticleId());
            pstmt.setString(2, reply.getWriterId());
            pstmt.setString(3, reply.getContents());
            pstmt.setBoolean(4, false);
            pstmt.setObject(5, reply.getFormattedCreatedAt());
            pstmt.setObject(6, reply.getFormattedUpdatedAt());
            pstmt.executeUpdate();

            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    reply.setId(resultSet.getLong(1));
                    return reply.getId();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Optional<Reply> findById(Long replyId) {
        String sql = "SELECT * FROM replies WHERE reply_id = ? AND is_deleted = false";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, replyId);

            try (var resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Reply(resultSet.getLong("reply_id"),
                            resultSet.getLong("article_id"),
                            resultSet.getString("writer_id"),
                            resultSet.getString("contents"),
                            resultSet.getTimestamp("created_at").toLocalDateTime(),
                            resultSet.getTimestamp("modified_at").toLocalDateTime()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        List<Reply> replies = new ArrayList<>();
        String sql = "SELECT * FROM replies WHERE article_id = ? AND is_deleted = false";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);

            try (var resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    replies.add(new Reply(resultSet.getLong("reply_id"),
                            resultSet.getLong("article_id"),
                            resultSet.getString("writer_id"),
                            resultSet.getString("contents"),
                            resultSet.getTimestamp("created_at").toLocalDateTime(),
                            resultSet.getTimestamp("modified_at").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return replies;
    }

    @Override
    public List<Reply> findByPage(final Long articleId, final int index, final int size) {
        List<Reply> replies = new ArrayList<>();
        String sql = "SELECT * FROM replies WHERE article_id = ? AND reply_id > ? AND is_deleted = false ORDER BY created_at asc LIMIT ?";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.setInt(2, index);
            pstmt.setInt(3, size);

            try (var resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    replies.add(new Reply(resultSet.getLong("reply_id"),
                            resultSet.getLong("article_id"),
                            resultSet.getString("writer_id"),
                            resultSet.getString("contents"),
                            resultSet.getTimestamp("created_at").toLocalDateTime(),
                            resultSet.getTimestamp("modified_at").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return replies;
    }

    @Override
    public List<Reply> findAll() {
        List<Reply> replies = new ArrayList<>();
        String sql = "SELECT * FROM replies";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql);
             var resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                replies.add(new Reply(resultSet.getLong("reply_id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return replies;
    }

    @Override
    public Optional<Reply> update(Reply reply) {
        String sql = "UPDATE replies SET contents = ?, modified_at = ? WHERE reply_id = ?";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reply.getContents());
            pstmt.setObject(2, Timestamp.valueOf(reply.getUpdatedAt()));
            pstmt.setLong(3, reply.getId());

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                return Optional.of(reply);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long replyId) {
        String sql = "UPDATE replies SET is_deleted = true WHERE reply_id = ?";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, replyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByArticleId(final Long articleId) {
        String sql = "UPDATE replies SET is_deleted = true WHERE article_id = ?";
        try (var connection = dataSource.getConnection();
             var pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
