package woowa.camp.jspcafe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.fixture.ArticleFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.DBArticleRepository;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;
import woowa.camp.jspcafe.utils.time.DateTimeProvider;

class ArticleRepositoryTest {

    DateTimeProvider fixedDateTime = new FixedDateTimeProvider(2024, 7, 23);

    @Nested
    @DisplayName("Describe_게시글을 저장하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class SaveTest {

        DatabaseConnector connector = new DatabaseConnector();
        ArticleRepository repository = new DBArticleRepository(connector);

        @Test
        @DisplayName("[Success] 게시글을 저장하면, 게시글 ID로 조회할 수 있다")
        void test() {
            // given
            Article article = ArticleFixture.createArticle1(fixedDateTime.getNow());

            // when
            Long savedId = repository.save(article);

            // then
            assertThat(repository.findById(savedId)).isPresent()
                    .get().satisfies(savedArticle -> {
                        assertThat(savedArticle.getId()).isEqualTo(savedId);
                        assertThat(savedArticle.getTitle()).isEqualTo(article.getTitle());
                        assertThat(savedArticle.getContent()).isEqualTo(article.getContent());
                    });

        }

        @Test
        @DisplayName("[Success] 게시글을 저장할 때마다, id가 1씩 증가한다")
        void test2() {
            // given
            Article article1 = ArticleFixture.createArticle1(fixedDateTime.getNow());
            Article article2 = ArticleFixture.createArticle2(fixedDateTime.getNow());

            // when
            Long savedId1 = repository.save(article1);
            Long savedId2 = repository.save(article2);

            // then
            assertThat(savedId2).isEqualTo(savedId1 + 1);
        }

        @Test
        @DisplayName("[Success] 게시글 작성자 id가 null 이면 익명게시판으로서 게시판 저장에 성공한다.")
        void test3() {
            Article anonymousArticle = Article.create(null, "title", "content", fixedDateTime.getNow());

            Long savedId = repository.save(anonymousArticle);

            Optional<Article> foundArticleOpt = repository.findById(savedId);
            Assertions.assertThat(foundArticleOpt).isPresent();
            Article foundArticle = foundArticleOpt.get();

            assertThat(foundArticle.getId()).isEqualTo(savedId);
            assertThat(foundArticle.getAuthorId()).isNull();
            assertThat(foundArticle.getTitle()).isEqualTo(anonymousArticle.getTitle());
            assertThat(foundArticle.getContent()).isEqualTo(anonymousArticle.getContent());
            assertThat(foundArticle.getHits()).isEqualTo(anonymousArticle.getHits());
            assertThat(foundArticle.getCreatedAt()).isEqualTo(anonymousArticle.getCreatedAt());
        }

    }

    @Nested
    @DisplayName("Describe_게시글을 id 기준으로 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class FindByIdTest {

        DatabaseConnector connector = new DatabaseConnector();
        ArticleRepository repository = new DBArticleRepository(connector);

        @Test
        @DisplayName("[Success] 특정 게시글을 조회할 수 있다")
        void test() {
            // given
            Article article = ArticleFixture.createArticle1(fixedDateTime.getNow());
            Long savedId = repository.save(article);

            // when
            Article foundArticle = repository.findById(savedId).orElseThrow();

            // then
            assertThat(foundArticle.getId()).isEqualTo(savedId);
            assertThat(foundArticle.getAuthorId()).isEqualTo(article.getAuthorId());
            assertThat(foundArticle.getTitle()).isEqualTo(article.getTitle());
            assertThat(foundArticle.getContent()).isEqualTo(article.getContent());
            assertThat(foundArticle.getHits()).isEqualTo(article.getHits());
            assertThat(foundArticle.getCreatedAt()).isEqualTo(article.getCreatedAt());
        }

        @Test
        @DisplayName("[Success] 존재하지 않는 ID로 조회하면 빈 Optional을 반환한다")
        void test2() {
            // given
            Long nonExistentId = 999L;

            // when
            var result = repository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[Success] ID가 null 이면 빈 Optional을 반환한다")
        void test3() {
            // given
            Long nonExistentId = null;

            // when
            var result = repository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("Describe_이전 게시글을 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class FindPreviousTest {

        DatabaseConnector connector = new DatabaseConnector();
        ArticleRepository repository = new DBArticleRepository(connector);

        @Test
        @DisplayName("[Success] 이전 게시글을 조회할 수 있다")
        void test() {
            // given
            Article article1 = ArticleFixture.createArticle1(fixedDateTime.getNow());
            Article article2 = ArticleFixture.createArticle2(fixedDateTime.getNow());
            repository.save(article1);
            repository.save(article2);

            // when
            Article previousArticle = repository.findPrevious(2L).orElseThrow();

            // then
            assertThat(previousArticle.getId()).isEqualTo(article1.getId());
            assertThat(previousArticle.getTitle()).isEqualTo(article1.getTitle());
        }

        @Test
        @DisplayName("[Success] 첫 번째 게시글의 이전 게시글을 조회하면 빈 Optional을 반환한다")
        void test2() {
            // given
            repository.save(ArticleFixture.createArticle1(fixedDateTime.getNow()));

            // when
            var result = repository.findPrevious(1L);

            // then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("Describe_다음 게시글을 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class FindNextTest {

        DatabaseConnector connector = new DatabaseConnector();
        ArticleRepository repository = new DBArticleRepository(connector);

        @Test
        @DisplayName("[Success] 다음 게시글을 조회할 수 있다")
        void test() {
            // given
            Article article1 = ArticleFixture.createArticle1(fixedDateTime.getNow());
            Article article2 = ArticleFixture.createArticle2(fixedDateTime.getNow());
            repository.save(article1);
            repository.save(article2);

            // when
            Article nextArticle = repository.findNext(1L).orElseThrow();

            // then
            assertThat(nextArticle.getId()).isEqualTo(2L);
            assertThat(nextArticle.getTitle()).isEqualTo(article2.getTitle());
        }

        @Test
        @DisplayName("[Success] 마지막 게시글의 다음 게시글을 조회하면 빈 Optional을 반환한다")
        void test2() {
            // given
            repository.save(ArticleFixture.createArticle1(fixedDateTime.getNow()));

            // when
            var result = repository.findNext(1L);

            // then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("Describe_오프셋 기반으로 게시글을 조회하는 기능은")
    @ExtendWith(ArticleDBSetupExtension.class)
    class FindByOffsetPaginationTest {

        DatabaseConnector connector = new DatabaseConnector();
        ArticleRepository repository = new DBArticleRepository(connector);
        private static final int PAGE_SIZE = 10;

        @Test
        @DisplayName("[Success] 첫 페이지의 게시글을 조회할 수 있다")
        void test() {
            // given
            List<Article> articles = ArticleFixture.createMultipleArticles(15, fixedDateTime.getNow());
            articles.forEach(repository::save);

            // when
            List<Article> firstPage = repository.findByOffsetPagination(0, PAGE_SIZE);
            System.out.println("firstPage = " + firstPage);
            // then
            assertThat(firstPage).hasSize(PAGE_SIZE);
            assertThat(firstPage.get(0).getId()).isEqualTo(15L);
            assertThat(firstPage.get(9).getId()).isEqualTo(6L);
        }

        @Test
        @DisplayName("[Success] 마지막 페이지의 게시글을 조회할 수 있다")
        void testLastPage() {
            // given
            List<Article> articles = ArticleFixture.createMultipleArticles(12, fixedDateTime.getNow());
            articles.forEach(repository::save);

            // when
            List<Article> lastPage = repository.findByOffsetPagination(10, PAGE_SIZE);

            // then
            assertThat(lastPage).hasSize(2);
            assertThat(lastPage.get(0).getId()).isEqualTo(2L);
            assertThat(lastPage.get(1).getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("[Success] 중간 페이지의 게시글을 조회할 수 있다")
        void testMiddlePage() {
            // given
            List<Article> articles = ArticleFixture.createMultipleArticles(25, fixedDateTime.getNow());
            articles.forEach(repository::save);

            // when
            List<Article> middlePage = repository.findByOffsetPagination(PAGE_SIZE, 10);

            // then
            assertThat(middlePage).hasSize(PAGE_SIZE);
            assertThat(middlePage.get(0).getId()).isEqualTo(15L);
            assertThat(middlePage.get(9).getId()).isEqualTo(6L);
        }

        @Test
        @DisplayName("[Success] offset이 전체 게시글 수를 초과하면 빈 리스트를 반환한다")
        void testOffsetExceedsTotalCount() {
            // given
            List<Article> articles = ArticleFixture.createMultipleArticles(5, fixedDateTime.getNow());
            articles.forEach(repository::save);

            // when
            List<Article> result = repository.findByOffsetPagination(PAGE_SIZE, 10);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[Success] 전체 게시글이 없을 때 빈 리스트를 반환한다")
        void testEmptyRepository() {
            // when
            List<Article> result = repository.findByOffsetPagination(PAGE_SIZE, 0);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[Success] 게시글을 생성일자 기준으로 내림차순 정렬한다")
        void orderedCreatedAtDesc() {
            Article article1 = ArticleFixture.createArticle1(fixedDateTime.getNow());
            Article article2 = ArticleFixture.createArticle2(fixedDateTime.getNow().plusDays(1L));
            Article article3 = ArticleFixture.createArticle3(fixedDateTime.getNow().plusDays(2L));

            repository.save(article3);
            repository.save(article1);
            repository.save(article2);

            List<Article> orderedResults = repository.findByOffsetPagination(0, 10);
            assertThat(orderedResults)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(article3, article2, article1);
        }

    }


}