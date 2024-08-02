package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.db.CommentDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.Article;
import cafe.domain.entity.Comment;
import cafe.domain.entity.User;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {
    private static UserDatabase userDatabase;
    private static ArticleDatabase articleDatabase;
    private static CommentDatabase commentDatabase;
    private static CommentService commentService;

    @BeforeAll
    static void setUp() {
        DatabaseConnector databaseConnector = new H2Connector();
        userDatabase = new UserDatabase(databaseConnector);
        articleDatabase = new ArticleDatabase(databaseConnector);
        commentDatabase = new CommentDatabase(databaseConnector);
        commentService = new CommentService(commentDatabase);
    }

    @AfterEach
    void tearDown() {
        userDatabase.deleteHardAll();
        articleDatabase.deleteHardAll();
        commentDatabase.deleteHardAll();
    }

    @Test
    void 올바른_댓글을_저장한다() {
        // given
        String commentId = "id";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));

        // when
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // then
        Comment comment = commentDatabase.selectById(commentId);
        assertEquals(userId, comment.getUserId());
        assertEquals(articleId, comment.getArticleId());
        assertEquals(contents, comment.getContents());
    }

    @Test
    void 경로의_아이디로_댓글을_삭제한다() {
        // given
        String commentId = "id";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // when
        commentService.deleteById("/comments/" + commentId);

        // then
        assertNull(commentDatabase.selectById(commentId));
    }

    @Test
    void 현재_유저가_댓글의_작성자가_맞는지_확인한다() {
        // given
        String commentId = "id";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // when
        User user = userDatabase.selectById(userId);
        commentService.verifyCommentId(user, "/comments/" + commentId);

        // then
        assertDoesNotThrow(() -> commentService.verifyCommentId(user, "/comments/" + commentId));
    }

    @Test
    void 현재_유저가_글_내의_모든_댓글의_작성자가_맞는지_확인한다() {
        // given
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of("id1", userId, articleId, contents));
        commentDatabase.insert(Comment.of("id2", userId, articleId, "contents2"));

        // when
        User user = userDatabase.selectById(userId);
        commentService.verifyCommentIdByArticleId(user, "/comments/" + articleId);

        // then
        assertDoesNotThrow(() -> commentService.verifyCommentIdByArticleId(user, "/comments/" + articleId));
    }

    @Test
    void 경로의_글_아이디로_댓글을_삭제한다() {
        // given
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of("id1", userId, articleId, contents));
        commentDatabase.insert(Comment.of("id2", userId, articleId, "contents2"));

        // when
        commentService.deleteByArticleId("/comments/" + articleId + "/delete");

        // then
        assertEquals(0, commentDatabase.selectCommentsByArticleId(articleId).size());
    }

    @Test
    void 현재_유저가_댓글의_작성자가_아니라면_예외가_발생한다() {
        // given
        String commentId = "id";
        String userId = "userId";
        String userId2 = "userId2";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        userDatabase.insert(User.of(userId2, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // when
        User user = userDatabase.selectById(userId2);

        // then
        assertThrows(IllegalArgumentException.class, () -> commentService.verifyCommentId(user, "/comments/" + commentId));
    }

    @Test
    void 현재_유저가_글_내의_모든_댓글의_사용자가_아니라면_예외가_발생한다() {
        // given
        String userId = "userId";
        String userId2 = "userId2";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        userDatabase.insert(User.of(userId2, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of("id1", userId, articleId, contents));
        commentDatabase.insert(Comment.of("id2", userId, articleId, "contents2"));

        // when
        User user = userDatabase.selectById(userId2);

        // then
        assertThrows(IllegalArgumentException.class, () -> commentService.verifyCommentIdByArticleId(user, "/comments/" + articleId));
    }
}