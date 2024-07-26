package com.hyeonuk.jspcafe.article.dao;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryArticleDao 클래스")
class InMemoryArticleDaoTest {

    private ArticleDao articleDao;

    @BeforeEach
    void setUp() {
        articleDao = new InMemoryArticleDao();
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("유효한 게시글을 저장하고 반환한다")
        void saveValidArticle() {
            Article article = new Article("writer", "title", "contents");
            Article savedArticle = articleDao.save(article);

            assertNotNull(savedArticle.getId());
            assertEquals(article.getWriter(), savedArticle.getWriter());
            assertEquals(article.getTitle(), savedArticle.getTitle());
            assertEquals(article.getContents(), savedArticle.getContents());
        }

        @Test
        @DisplayName("이미 존재하는 게시글을 저장하면 업데이트된다.")
        void updateArticle(){
            Article article = new Article("writer", "title", "contents");
            articleDao.save(article);

            article.setContents("updatedContent");
            article.setTitle("updatedTitle");
            Article save = articleDao.save(article);

            assertEquals("updatedTitle", save.getTitle());
            assertEquals("updatedContent", save.getContents());
        }

        @Nested
        @DisplayName("유효하지 않은 기사를 저장하려고 하면 예외를 던진다")
        class SaveInvalidArticleThrowsException {

            @Test
            @DisplayName("writer가 null이면 예외를 던진다")
            void saveWithNullWriter() {
                Article article = new Article(null, "title", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("title이 null이면 예외를 던진다")
            void saveWithNullTitle() {
                Article article = new Article("writer", null, "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("contents가 null이면 예외를 던진다")
            void saveWithNullContents() {
                Article article = new Article("writer", "title", null);
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("writer가 빈 문자열이면 예외를 던진다")
            void saveWithEmptyWriter() {
                Article article = new Article("", "title", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("title이 빈 문자열이면 예외를 던진다")
            void saveWithEmptyTitle() {
                Article article = new Article("writer", "", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("contents가 빈 문자열이면 예외를 던진다")
            void saveWithEmptyContents() {
                Article article = new Article("writer", "title", "");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("모든 게시글을 반환한다")
        void findAllArticles() {
            Article article1 = new Article("writer1", "title1", "contents1");
            Article article2 = new Article("writer2", "title2", "contents2");
            articleDao.save(article1);
            articleDao.save(article2);

            List<Article> articles = articleDao.findAll();
            assertEquals(2, articles.size());
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("ID로 게시글을 찾고 반환한다")
        void findArticleById() {
            Article article = new Article("writer", "title", "contents");
            Article savedArticle = articleDao.save(article);

            Optional<Article> foundArticle = articleDao.findById(savedArticle.getId());
            assertTrue(foundArticle.isPresent());
            assertEquals(savedArticle.getId(), foundArticle.get().getId());
        }

        @Test
        @DisplayName("존재하지 않는 ID로 게시글을 찾으려고 하면 빈 Optional을 반환한다")
        void findNonExistingArticleById() {
            Optional<Article> foundArticle = articleDao.findById(999L);
            assertFalse(foundArticle.isPresent());
        }
    }
}
