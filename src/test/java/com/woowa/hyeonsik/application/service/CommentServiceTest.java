package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.application.MemoryDbTest;
import com.woowa.hyeonsik.application.dao.ArticleDao;
import com.woowa.hyeonsik.application.dao.CommentDao;
import com.woowa.hyeonsik.application.dao.JdbcArticleDao;
import com.woowa.hyeonsik.application.dao.JdbcCommentDao;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.domain.Reply;
import com.woowa.hyeonsik.application.exception.AuthenticationException;
import com.woowa.hyeonsik.server.database.DatabaseConnector;
import com.woowa.hyeonsik.server.database.property.H2Property;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest extends MemoryDbTest {
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        CommentDao commentDao = new JdbcCommentDao(new DatabaseConnector(new H2Property()));
        ArticleDao articleDao = new JdbcArticleDao(new DatabaseConnector(new H2Property()));
        commentService = new CommentService(commentDao, articleDao);

        articleDao.save(new Article(1L, "TEST_USER", "제목입니다", "내용입니다"));
    }

    @Test
    @DisplayName("게시글에 댓글을 성공적으로 작성한다.")
    void addComment() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");

        commentService.addComment(reply);

        Reply foundReply = commentService.findAllByArticleId(1L).get(0);
        assertThat(foundReply.getArticleId()).isEqualTo(1L);
        assertThat(foundReply.getWriter()).isEqualTo("TEST_USER");
        assertThat(foundReply.getContents()).isEqualTo("COMMENT");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글을 달면 예외가 발생한다.")
    void addComment_emptyArticle() {
        Reply reply = new Reply(null, 2L, "TEST_USER", "COMMENT");

        assertThrows(IllegalArgumentException.class, () -> commentService.addComment(reply));
    }

    @Test
    @DisplayName("게시글에 댓글을 모두 읽어온다.")
    void findAllByArticleId() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        Reply reply2 = new Reply(null, 1L, "TEST_USER_USER", "HIHELLO");
        commentService.addComment(reply);
        commentService.addComment(reply2);

        List<Reply> all = commentService.findAllByArticleId(1L);

        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글목록을 불러오면 예외가 난다.")
    void findAllByArticleId_emptyArticle() {
        assertThrows(IllegalArgumentException.class, () -> commentService.findAllByArticleId(2L));
    }

    @Test
    @DisplayName("댓글의 내용을 성공적으로 수정한다.")
    void updateComment() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        commentService.addComment(reply);

        Reply newReply = new Reply(1L, 1L, "TEST_USER", "CHANGED");
        commentService.updateComment(newReply, "TEST_USER");

        Reply foundReply = commentService.findAllByArticleId(1L).get(0);
        assertThat(foundReply.getArticleId()).isEqualTo(1L);
        assertThat(foundReply.getWriter()).isEqualTo("TEST_USER");
        assertThat(foundReply.getContents()).isEqualTo("CHANGED");
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우 댓글 수정시 예외가 발생한다.")
    void updateComment_emptyComment() {
        Reply newReply = new Reply(1L, 1L, "TEST_USER", "CHANGED");

        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(newReply, "TEST_USER"));
    }

    @Test
    @DisplayName("다른 사람이 쓴 댓글 수정시 예외가 발생한다.")
    void updateComment_notMyOwnReply() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        Reply newReply = new Reply(1L, 1L, "TEST_USER", "CHANGED");
        commentService.addComment(reply);

        assertThrows(AuthenticationException.class, () -> commentService.updateComment(newReply, "ANOTHER_USER"));
    }

    @Test
    @DisplayName("댓글을 성공적으로 삭제한다.")
    void deleteComment() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        commentService.addComment(reply);

        commentService.deleteComment(1L, "TEST_USER");
    }

    @Test
    @DisplayName("댓글이 존재하지않는 경우 댓글 삭제시 예외가 발생한다.")
    void deleteComment_notMyOwnReply() {
        assertThrows(IllegalArgumentException.class, () -> commentService.deleteComment(1L, "TEST_USER"));
    }

    @Test
    @DisplayName("다른 사람이 쓴 댓글 삭제시 예외가 발생한다.")
    void deleteComment_notMyReply() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        commentService.addComment(reply);

        assertThrows(AuthenticationException.class, () -> commentService.deleteComment(1L, "ANOTHER_USER"));
    }
}
