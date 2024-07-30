package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.util.LocalDateTimeUtil;
import com.woowa.hyeonsik.server.database.DatabaseConnector;

import com.woowa.hyeonsik.server.database.JdbcException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcArticleDao implements ArticleDao {
    private final DatabaseConnector databaseConnector;

    public JdbcArticleDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void save(Article article) {
        String sql = "INSERT INTO article(writer_id, title, contents, create_at) VALUES(?, ?, ?, ?)";
        databaseConnector.execute(sql,
                List.of(article.getWriter(), article.getTitle(), article.getContents(), article.getCreatedAt().toString()));
    }

    @Override
    public Optional<Article> findByArticleId(long articleId) {
        String sql = """
            SELECT 
                article_id,
                writer_id,
                title,
                contents,
                create_at,
                modified_at
            FROM
                article
            WHERE
                article_id = ? 
            """;

        return databaseConnector.executeQuery(sql, List.of(String.valueOf(articleId)),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            Long id = resultSet.getLong("article_id");
                            String writerId = resultSet.getString("writer_id");
                            String title = resultSet.getString("title");
                            String contents = resultSet.getString("contents");
                            LocalDateTime createAt = LocalDateTimeUtil.from(resultSet.getString("create_at"));
                            return Optional.of(new Article(id, writerId, title, contents, createAt));
                        }
                        return Optional.empty();
                    } catch (SQLException e) {
                        throw new JdbcException(e);
                    }
                }
        );
    }

    @Override
    public List<Article> findAll() {
        String sql = """
            SELECT 
                article_id,
                writer_id,
                title,
                contents,
                create_at,
                modified_at
            FROM
                article
            """;

        return databaseConnector.executeQuery(sql,
                resultSet -> {
                    try {
                        List<Article> list = new ArrayList<>();
                        while (resultSet.next()) {
                            Long id = resultSet.getLong("article_id");
                            String writerId = resultSet.getString("writer_id");
                            String title = resultSet.getString("title");
                            String contents = resultSet.getString("contents");
                            LocalDateTime createAt = LocalDateTimeUtil.from(resultSet.getString("create_at"));
                            list.add(new Article(id, writerId, title, contents, createAt));
                        }
                        return list;
                    } catch (SQLException e) {
                        throw new JdbcException(e);
                    }
                }
        );
    }

    @Override
    public void update(Article article) {
        String sql = """
                UPDATE article
                SET title = ?, contents = ?
                WHERE article_id = ?
                """;

        databaseConnector.execute(sql, List.of(article.getTitle(), article.getContents(), String.valueOf(article.getId())));
    }
}
