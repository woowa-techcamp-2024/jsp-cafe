package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.domain.Page;
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
                article_id = ? AND 
                is_deleted = 0
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
    public Page<Article> findAll(long page) {
        final int AMOUNT = 15;

        if (page <= 0) {
            throw new IllegalArgumentException("페이지 값은 양수여야합니다. 현재값: " + page);
        }

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
                is_deleted = 0
            ORDER BY 
                article_id DESC
            LIMIT ? OFFSET ? 
            """;

        long offset = (page - 1) * AMOUNT;

        List<Article> articles = databaseConnector.executeQuery(sql, List.of(AMOUNT, offset),
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

        // 마지막 페이지 넘버 구하기
        String sql2 = """
                SELECT count(*) as c FROM article
                """;
        Long numberOfEnd = databaseConnector.executeQuery(sql2,
                resultSet -> {
                    try {
                        if (!resultSet.next()) {
                            return 0L;
                        }
                        return resultSet.getLong("c");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        return new Page<>(page,
                numberOfEnd / AMOUNT + 1,
                articles
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

    @Override
    public void removeByArticleId(long articleId) {
        String sql = """
            UPDATE article
            SET is_deleted = 1
            WHERE article_id = ?
            """;

        databaseConnector.execute(sql, List.of(String.valueOf(articleId)));
    }
}
