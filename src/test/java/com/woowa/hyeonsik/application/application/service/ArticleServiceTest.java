package com.woowa.hyeonsik.application.application.service;

import com.woowa.hyeonsik.application.application.MemoryDbTest;
import com.woowa.hyeonsik.application.dao.JdbcArticleDao;
import com.woowa.hyeonsik.application.dao.JdbcCommentDao;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.domain.Reply;
import com.woowa.hyeonsik.application.exception.AuthorizationException;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.server.database.DatabaseConnector;
import com.woowa.hyeonsik.server.database.property.H2Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleServiceTest extends MemoryDbTest {
    private ArticleService articleService;
    private JdbcArticleDao articleDao;
    private JdbcCommentDao commentDao;

    @BeforeEach
    void setUp() {
        articleDao = new JdbcArticleDao(new DatabaseConnector(new H2Property()));
        commentDao = new JdbcCommentDao(new DatabaseConnector(new H2Property()));
        articleService = new ArticleService(articleDao, commentDao);
    }

    @Test
    @DisplayName("정상적으로 글을 작성한다.")
    void write() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");

        articleService.write(article);
        Article foundArticle = articleService.findById(1);

        assertThat(foundArticle.getWriter()).isEqualTo("TEST_USER");
        assertThat(foundArticle.getTitle()).isEqualTo("테스트 글입니다");
        assertThat(foundArticle.getContents()).isEqualTo("안녕하세요? 반갑습니다 ^^");
    }

    @Test
    @DisplayName("존재하지 않는 글을 조회하면 예외가 발생한다.")
    void read_exception() {
        assertThrows(IllegalArgumentException.class, () -> articleService.findById(12345));
    }

    @Test
    @DisplayName("정상적으로 글 목록을 확인한다.")
    void list() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");

        articleService.write(article);
        articleService.write(article);
        articleService.write(article);
        List<Article> list = articleService.list();

        assertThat(list).hasSize(3);
    }

    @Test
    @DisplayName("정상적으로 글을 수정한다.")
    void update_article() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        articleDao.save(article);

        Article updateArticle = new Article(1L, "TEST_USER", "제목입니다", "그래요?");  //TODO REQUEST를 별도로 분리해야 할 듯
        articleService.update(updateArticle, "TEST_USER");
        Article foundArticle = articleService.findById(1L);

        assertThat(foundArticle.getId()).isEqualTo(1L);
        assertThat(foundArticle.getWriter()).isEqualTo("TEST_USER");
        assertThat(foundArticle.getTitle()).isEqualTo("제목입니다");
        assertThat(foundArticle.getContents()).isEqualTo("그래요?");
    }

    @Test
    @DisplayName("존재하지 않는 글을 수정하면 예외가 발생한다.")
    void update_illegal_article_exception() {
        Article updateArticle = new Article(1L, "TEST_USER", "제목입니다", "그래요?");  //TODO REQUEST를 별도로 분리해야 할 듯

        assertThrows(IllegalArgumentException.class, () -> articleService.update(updateArticle, "TEST_USER"));
    }

    @Test
    @DisplayName("다른 유저의 글을 수정하면 예외가 발생한다.")
    void update_article_exception() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        articleDao.save(article);

        Article updateArticle = new Article(1L, "TEST_USER", "제목입니다", "그래요?");  //TODO REQUEST를 별도로 분리해야 할 듯
        assertThrows(AuthorizationException.class, () -> articleService.update(updateArticle, "ANOTHER_USER"));
    }

    @Test
    @DisplayName("정상적으로 게시글을 삭제한다.")
    void remove_article() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        articleDao.save(article);

        articleService.remove(1L, "TEST_USER");

        List<Article> all = articleDao.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 글을 수정하면 예외가 발생한다.")
    void remove_illegal_article_exception() {
        assertThrows(IllegalArgumentException.class, () -> articleService.remove(1L, "TEST_USER"));
    }

    @Test
    @DisplayName("다른 유저의 글을 삭제하면 예외가 발생한다.")
    void remove_article_exception() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        articleDao.save(article);

        assertThrows(AuthorizationException.class, () -> articleService.remove(1L, "ANOTHER_USER"));
    }

    @Test
    @DisplayName("게시글 삭제시, 내가 쓴 댓글만 존재하면 정상 삭제한다.")
    void remove_article_with_my_comment() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        Reply reply = new Reply(null, 1L, "TEST_USER", "HIHIHI");
        articleDao.save(article);
        commentDao.save(reply);

        articleService.remove(1L, "TEST_USER");
        assertThat(articleDao.findAll()).isEmpty();
    }

    @Test
    @DisplayName("게시글 삭제시, 다른 사람이 쓴 댓글이 존재하면 예외가 발생한다.")
    void remove_article_with_another_comment_exception() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        Reply reply = new Reply(null, 1L, "ANOTHER_USER", "HIHIHI");
        articleDao.save(article);
        commentDao.save(reply);

        assertThrows(IllegalStateException.class, () -> articleService.remove(1L, "TEST_USER"));
    }

    @Test
    @DisplayName("게시글 삭제시, 다른 사람이 댓글을 작성하고 삭제했어도 정상 처리된다.")
    void remove_article_with_another_comment_history() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");
        Reply reply = new Reply(null, 1L, "ANOTHER_USER", "HIHIHI");
        articleDao.save(article);
        commentDao.save(reply);
        commentDao.removeByReplyId(1L);

        articleService.remove(1L, "TEST_USER");
        assertThat(articleDao.findAll()).isEmpty();
    }
}
