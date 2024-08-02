package cafe.domain.db;

import cafe.domain.entity.Article;
import cafe.domain.entity.Comment;
import cafe.domain.entity.User;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CommentDatabaseTest {
    private static DatabaseConnector connector;
    private static UserDatabase userDatabase;
    private static ArticleDatabase articleDatabase;
    private static CommentDatabase commentDatabase;

    @BeforeAll
    static void setUp() {
        connector = new H2Connector();
        userDatabase = new UserDatabase(connector);
        articleDatabase = new ArticleDatabase(connector);
        commentDatabase = new CommentDatabase(connector);
    }

    @AfterEach
    void tearDown() {
        userDatabase.deleteAll();
        articleDatabase.deleteAll();
        commentDatabase.deleteAll();
    }

    @Test
    void 올바른_댓글을_추가한다() {
        // given
        String commentId = "commentId";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";

        // when
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // then
        Comment comment = commentDatabase.selectById(commentId);
        assertEquals(comment.getCommentId(), commentId);
        assertEquals(comment.getUserId(), userId);
        assertEquals(comment.getArticleId(), articleId);
        assertEquals(comment.getContents(), contents);
    }

    @Test
    void 조회_시_아이디에_맞는_댓글이_없으면_null을_반환한다() {
        // given, when
        Comment comment = commentDatabase.selectById("commentId");

        // then
        assertNull(comment);
    }

    @Test
    void 글에_해당하는_모든_댓글을_조회한다() {
        // given
        String userId = "userId";
        String articleId = "articleId";
        String contents1 = "contents1";
        String contents2 = "contents2";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of("commentId1", userId, articleId, contents1));
        commentDatabase.insert(Comment.of("commentId2", userId, articleId, contents2));

        // when
        var comments = commentDatabase.selectCommentsByArticleId(articleId);

        // then
        assertEquals(2, comments.size());
        assertEquals(comments.get(0).getContents(), contents1);
        assertEquals(comments.get(1).getContents(), contents2);
    }

    @Test
    void 조회_시_글에_해당하는_댓글이_없으면_빈_list를_반환한다() {
        // given, when, then
        assertEquals(new ArrayList<>(), commentDatabase.selectCommentsByArticleId("articleId"));
    }

    @Test
    void 아이디에_맞는_댓글을_삭제한다() {
        // given
        String commentId = "commentId";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";
        userDatabase.insert(User.of(userId, "name", "password", "email@email"));
        articleDatabase.insert(Article.of(articleId, userId, "title", "contents"));
        commentDatabase.insert(Comment.of(commentId, userId, articleId, contents));

        // when
        commentDatabase.deleteById(commentId);

        // then
        assertNull(commentDatabase.selectById(commentId));
    }
}