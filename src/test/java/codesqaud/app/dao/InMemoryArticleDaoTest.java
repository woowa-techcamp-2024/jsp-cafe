package codesqaud.app.dao;

import codesqaud.app.dao.article.InMemoryArticleDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class InMemoryArticleDaoTest {
    private InMemoryArticleDao articleDao;
    private Article article1;
    private Article article2;
    private Article article3;

    @BeforeEach
    public void setUp() {
        articleDao = new InMemoryArticleDao();

        article1 = new Article("title1", "content1", 1L);
        article2 = new Article( "title2", "content2", 1L);
        article3 = new Article( "title3", "content3", 1L);
    }

    @Nested
    class Save_할_때 {
        @Test
        void 저장된_기사가_없으면_생성한다() {
            articleDao.save(article1);
            Optional<Article> findArticle = articleDao.findById(article1.getId());

            assertThat(findArticle).isPresent();
            assertThat(findArticle.get().getTitle()).isEqualTo("title1");
        }

        @Test
        void 저장할_때_id를_명시하면_예외가_발생한다() {
            article1.setId(1L);
            assertThatThrownBy(() -> articleDao.save(article1))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Nested
    class Update_할_때 {
        @Test
        void 저장된_기사를_업데이트한다() {
            articleDao.save(article1);
            article1.setTitle("newTitle");
            articleDao.update(article1);

            Optional<Article> findArticle = articleDao.findById(article1.getId());

            assertThat(findArticle).isPresent();
            assertThat(findArticle.get().getTitle()).isEqualTo("newTitle");
        }

        @Test
        void id가_없는_기사를_업데이트하면_예외가_발생한다() {
            assertThatThrownBy(() -> articleDao.update(article1))
                    .isInstanceOf(HttpException.class);
        }

        @Test
        void 없는_기사를_업데이트하면_예외가_발생한다() {
            article1.setId(999L);
            assertThatThrownBy(() -> articleDao.update(article1))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Nested
    class FindAll_할_때 {
        @Test
        void 모든_기사를_검색할_수_있다() {
            articleDao.save(article1);
            articleDao.save(article2);
            articleDao.save(article3);
            List<Article> articles = articleDao.findAll();

            assertThat(articles).hasSize(3)
                    .extracting(Article::getTitle).containsExactlyInAnyOrder("title1", "title2", "title3");
        }
    }

    @Nested
    class Delete_할_때 {
        @Test
        void 저장한_기사를_삭제할_수_있다() {
            articleDao.save(article1);
            articleDao.delete(article1);
            Optional<Article> findArticle = articleDao.findById(article1.getId());

            assertThat(findArticle).isNotPresent();
        }

        @Test
        void 저장하지_않은_기사를_삭제하려하면_예외가_발생한다() {
            article1.setId(1L);
            assertThatThrownBy(() -> articleDao.delete(article1))
                    .isInstanceOf(HttpException.class);
        }
    }

    @Test
    void 동시_요청시_충돌이_생기지_않는다() throws InterruptedException, ExecutionException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            futures.add(executor.submit(() -> {
                Article article = new Article( "title", "content", 1L);
                articleDao.save(article);
                articleDao.delete(article);
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        List<Article> articles = articleDao.findAll();
        assertThat(articles).isEmpty();
    }

    @Nested
    class Setter_유효성_검사 {
        private Article article;

        @BeforeEach
        void setUp() {
            article = new Article("title", "contents", 1L);
        }

        @Test
        void 제목이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setTitle(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void 제목이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setTitle(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("제목은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void 내용이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setContents(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void 내용이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> article.setContents(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("내용은 null이거나 비어있을 수 없습니다.");
        }
    }
}
