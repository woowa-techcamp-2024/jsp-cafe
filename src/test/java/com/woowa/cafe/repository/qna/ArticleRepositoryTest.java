package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.utils.DBContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ArticleRepositoryTest {

    ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DBContainer.getDataSource();
        articleRepository = new JdbcArticleRepository(dataSource);

        String dropArticleTable = "DROP TABLE IF EXISTS articles";
        String createArticleTable = "CREATE TABLE articles (" +
                "article_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "writer_id VARCHAR(255), " +
                "title VARCHAR(255), " +
                "contents TEXT, " +
                "reply_count BIGINT, " +
                "is_deleted BOOLEAN, " +
                "create_at TIMESTAMP, " +
                "modified_at TIMESTAMP, " +
                "index idx_article_is_deleted (is_deleted))";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(dropArticleTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(createArticleTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다.")
    void create() {
        // given
        Article article = new Article("title", "contents", "writer");

        // when
        Long savedArticle = articleRepository.save(article);

        Optional<Article> findArticle = articleRepository.findById(savedArticle);

        // then
        assertAll(
                () -> assertTrue(findArticle.isPresent()),
                () -> assertEquals(findArticle.get().getWriterId(), "writer"),
                () -> assertEquals(findArticle.get().getTitle(), "title"),
                () -> assertEquals(findArticle.get().getContents(), "contents")
        );
    }

    @Test
    @DisplayName("게시글을 수정할 수 있다.")
    void update() {
        // given
        Article article = new Article("title", "contents", "writer");
        Long savedArticle = articleRepository.save(article);

        Optional<Article> byId = articleRepository.findById(savedArticle);
        byId.get().update("update title", "update contents");

        // when
        articleRepository.update(byId.get());

        Optional<Article> findArticle = articleRepository.findById(savedArticle);

        // then
        assertAll(
                () -> assertTrue(findArticle.isPresent()),
                () -> assertEquals(findArticle.get().getWriterId(), "writer"),
                () -> assertEquals(findArticle.get().getTitle(), "update title"),
                () -> assertEquals(findArticle.get().getContents(), "update contents")
        );
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void delete() {
        // given
        Article article = new Article("writer", "title", "contents");
        Long savedArticle = articleRepository.save(article);

        // when
        articleRepository.delete(savedArticle);

        Optional<Article> findArticle = articleRepository.findById(savedArticle);

        // then
        assertFalse(findArticle.isPresent());
    }

    @Test
    @DisplayName("게시글을 조회할 수 있다.")
    void findById() {
        // given
        Article article = new Article("title", "contents", "writer");
        Long savedArticle = articleRepository.save(article);

        // when
        Optional<Article> findArticle = articleRepository.findById(savedArticle);

        // then
        assertAll(
                () -> assertTrue(findArticle.isPresent()),
                () -> assertEquals(findArticle.get().getWriterId(), "writer"),
                () -> assertEquals(findArticle.get().getTitle(), "title"),
                () -> assertEquals(findArticle.get().getContents(), "contents")
        );
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void findAll() {
        // given
        Article article1 = new Article("title1", "contents1", "writer1");
        Article article2 = new Article("title2", "contents2", "writer2");
        Article article3 = new Article("title3", "contents3", "writer3");

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        // when
        int size = articleRepository.findAll().size();

        // then
        assertEquals(size, 3);
    }
}
