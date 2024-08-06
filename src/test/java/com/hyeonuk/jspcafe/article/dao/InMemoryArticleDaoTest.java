package com.hyeonuk.jspcafe.article.dao;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import com.hyeonuk.jspcafe.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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
            Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title", "contents");
            Article savedArticle = articleDao.save(article);

            assertNotNull(savedArticle.getId());
            assertEquals(article.getWriter(), savedArticle.getWriter());
            assertEquals(article.getTitle(), savedArticle.getTitle());
            assertEquals(article.getContents(), savedArticle.getContents());
        }

        @Test
        @DisplayName("이미 존재하는 게시글을 저장하면 업데이트된다.")
        void updateArticle(){
            Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title", "contents");
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
                Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), null, "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("contents가 null이면 예외를 던진다")
            void saveWithNullContents() {
                Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title", null);
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("writer가 null이면 예외를 던진다.")
            void saveWithNullWriterId() {
                Article article = new Article(null, "title", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("writer의 id가 Null 이면 예외를 던진다.")
            void saveWithEmptyWriterId() {
                Article article = new Article(new Member(null,"id1","pw1","nickname1","email1"), "title", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("title이 빈 문자열이면 예외를 던진다")
            void saveWithEmptyTitle() {
                Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "", "contents");
                assertThrows(DataIntegrityViolationException.class, () -> articleDao.save(article));
            }

            @Test
            @DisplayName("contents가 빈 문자열이면 예외를 던진다")
            void saveWithEmptyContents() {
                Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title", "");
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
            Article article1 = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title1", "contents1");
            Article article2 = new Article(new Member(2l,"id2","pw2","nick2","email2"), "title2", "contents2");
            articleDao.save(article1);
            articleDao.save(article2);

            List<Article> articles = articleDao.findAll();
            assertEquals(2, articles.size());
        }
    }

    @Nested
    @DisplayName("findAll 메서드의 페이징 처리용 메서드는")
    class FindAllWithPaging {
        @Test
        @DisplayName("해당 페이지의 객체들을 반환한다.")
        void findAllArticlesWithPaging() {
            //given
            int total = 100;
            int size = 10;
            int page = 1;
            Member member = new Member(1l, "id1", "pw1", "nick1", "email1");
            List<Article> expected = new LinkedList<>();
            for(int i=1;i<=total;i++){
                Article article = new Article(member,"title"+i,"contents"+i);
                article = articleDao.save(article);
                if((page-1)*size <= i && i<page*size) {
                    expected.add(article);
                }
            }

            //when
            List<Article> articles = articleDao.findAll(size, page);

            //then
            List<Long> expectedArticleId = LongStream.rangeClosed(1,10)
                    .boxed()
                    .toList();
            List<Long> actual = articles.stream()
                    .map(Article::getId)
                    .toList();

            assertEquals(expectedArticleId,actual);
        }

        @Test
        @DisplayName("범위 밖의 요청이면 빈 리스트를 반환한다.")
        void findAllWithPagingWithoutRange() {
            //given
            int total = 100;
            int size = 10;
            int page = Integer.MAX_VALUE;
            Member member = new Member(1l, "id1", "pw1", "nick1", "email1");
            List<Article> expected = new LinkedList<>();
            for(int i=1;i<=total;i++){
                Article article = new Article(member,"title"+i,"contents"+i);
                article = articleDao.save(article);
                if((page-1)*size <= i && i<page*size) {
                    expected.add(article);
                }
            }

            //when
            List<Article> articles = articleDao.findAll(size, page);

            //then
            assertTrue(articles.isEmpty());
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("ID로 게시글을 찾고 반환한다")
        void findArticleById() {
            Article article = new Article(new Member(1l,"id1","pw1","nick1","email1"), "title", "contents");
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

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById{
        @Test
        @DisplayName("존재하는 article id면 삭제한다.")
        void deleteArticleByIdSuccess() throws Exception{
            //given
            Article article = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title","contents");
            articleDao.save(article);

            //when
            articleDao.deleteById(article.getId());

            //then
            assertTrue(articleDao.findById(article.getId()).isEmpty());
        }
    }
}
