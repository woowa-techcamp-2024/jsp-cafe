package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.application.MemoryDbTest;
import com.woowa.hyeonsik.application.dao.CommentDao;
import com.woowa.hyeonsik.application.dao.JdbcCommentDao;
import com.woowa.hyeonsik.application.domain.Reply;
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
        commentService = new CommentService(commentDao);
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
    @DisplayName("게시글에 댓글을 모두 읽어온다.")
    @Disabled
    void findAllByArticleId() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        Reply reply2 = new Reply(null, 1L, "TEST_USER_USER", "HIHELLO");
        commentService.addComment(reply);
        commentService.addComment(reply2);

        List<Reply> all = commentService.findAllByArticleId(1L);

        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("댓글의 내용을 성공적으로 수정한다.")
    @Disabled
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
    @DisplayName("댓글을 성공적으로 삭제한다.")
    @Disabled
    void deleteComment() {
        Reply reply = new Reply(null, 1L, "TEST_USER", "COMMENT");
        commentService.addComment(reply);

        commentService.deleteComment(1L, "TEST_USER");
    }
}
