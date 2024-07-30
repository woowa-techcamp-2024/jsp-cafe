package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Reply;
import com.woowa.hyeonsik.application.util.LocalDateTimeUtil;
import com.woowa.hyeonsik.server.database.DatabaseConnector;
import com.woowa.hyeonsik.server.database.JdbcException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcCommentDao implements CommentDao {
    private final DatabaseConnector databaseConnector;

    public JdbcCommentDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // id
    // article_id
    // writer_id
    // contents
    // created_at

    @Override
    public void save(Reply reply) {
        String sql = """
               INSERT INTO comment(article_id, writer_id, contents, created_at) 
               VALUES(?, ?, ?, ?) 
               """;

        databaseConnector.execute(sql, List.of(
                String.valueOf(reply.getArticleId()),
                reply.getWriter(),
                reply.getContents(),
                String.valueOf(reply.getCreatedAt())
        ));
    }

    @Override
    public List<Reply> findAllByArticleId(long articleId) {
        String sql = """
                SELECT id, article_id, writer_id, contents, created_at
                FROM comment
                WHERE article_id = ?
                """;

        return databaseConnector.executeQuery(sql, List.of(String.valueOf(articleId)),
                resultSet -> {
                    try {
                        List<Reply> list = new ArrayList<>();
                        while (resultSet.next()) {
                            Long id = resultSet.getLong("id");
                            Long articlesId = resultSet.getLong("article_id");
                            String writerId = resultSet.getString("writer_id");
                            String contents = resultSet.getString("contents");
                            LocalDateTime createdAt = LocalDateTimeUtil.from(resultSet.getString("created_at"));

                            list.add(new Reply(id, articlesId, writerId, contents, createdAt));
                        }
                        return list;
                    } catch (SQLException e) {
                        throw new JdbcException(e);
                    }
                });
    }

    @Override
    public void update(final Reply reply) {
        String sql = """
            UPDATE comment 
            SET contents = ? 
            WHERE id = ?
            """;

        databaseConnector.execute(sql, List.of(reply.getContents(), String.valueOf(reply.getId())));
    }

    @Override
    public void removeByReplyId(final long replyId) {
        String sql = """
            DELETE FROM comment
            WHERE id = ?
            """;

        databaseConnector.execute(sql, List.of(String.valueOf(replyId)));
    }
}
