package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Reply;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        try (var connection = dataSource.getConnection()) {
            String sql = "INSERT INTO replies (article_id, writer_id, contents, create_at, modified_at) VALUES (?, ?, ?, ?, ?)";
            var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, reply.getArticleId());
            pstmt.setString(2, reply.getWriterId());
            pstmt.setString(3, reply.getContents());
            pstmt.setObject(4, reply.getCreateAt());
            pstmt.setObject(5, reply.getUpdateAt());
            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                reply.setId(resultSet.getLong(1));
                return reply.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public Optional<Reply> findById(Long replyId) {
        try (var connection = dataSource.getConnection()) {
            var pstmt = connection.prepareStatement("SELECT * FROM replies WHERE reply_id = ?");
            pstmt.setLong(1, replyId);

            var resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Reply(resultSet.getLong("reply_id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("create_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        List<Reply> replies = new ArrayList<>();
        try (var connection = dataSource.getConnection()) {
            var pstmt = connection.prepareStatement("SELECT * FROM replies WHERE article_id = ?");
            pstmt.setLong(1, articleId);

            var resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                replies.add(new Reply(resultSet.getLong("reply_id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("create_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime()));
            }

            return replies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reply> findAll() {
        List<Reply> replies = new ArrayList<>();
        try (var connection = dataSource.getConnection()) {
            var pstmt = connection.prepareStatement("SELECT * FROM replies");

            var resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                replies.add(new Reply(resultSet.getLong("reply_id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("create_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime()));
            }

            return replies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Reply> update(Reply reply) {
        try (var connection = dataSource.getConnection()) {
            var pstmt = connection.prepareStatement("UPDATE replies SET contents = ?, modified_at = ? WHERE reply_id = ?");
            pstmt.setString(1, reply.getContents());
            pstmt.setObject(2, reply.getUpdateAt());
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
        try (var connection = dataSource.getConnection()) {
            var pstmt = connection.prepareStatement("DELETE FROM replies WHERE reply_id = ?");
            pstmt.setLong(1, replyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
