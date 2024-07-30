package cafe.domain.db;

import cafe.domain.entity.Article;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDatabaseTest {
    private static DatabaseConnector connector;
    private static ArticleDatabase articleDatabase;

    @BeforeAll
    static void setUp() {
        connector = new H2Connector();
        articleDatabase = new ArticleDatabase(connector);
    }

    @AfterEach
    void tearDown() {
        articleDatabase.deleteAll();
    }

    @Test
    void 올바른_글을_추가한다() {
        // given
        String id = "id";
        String writer = "writer";
        String title = "title";
        String contents = "contents";

        // when
        articleDatabase.insert(Article.of(id, writer, title, contents));
        Article article = articleDatabase.selectById(id);

        // then
        assertEquals(article.getArticleId(), id);
        assertEquals(article.getWriter(), writer);
        assertEquals(article.getTitle(), title);
        assertEquals(article.getContents(), contents);
    }

    @Test
    void 조회하는_글이_없으면_null을_반환한다() {
        // given
        String id = "id";

        // when
        Article article = articleDatabase.selectById(id);

        // then
        assertEquals(article, null);
    }

    @Test
    void 모든_글을_조회한다() {
        // given
        String id1 = "id1";
        String writer1 = "writer1";
        String title1 = "title1";
        String contents1 = "contents1";

        String id2 = "id2";
        String writer2 = "writer2";
        String title2 = "title2";
        String contents2 = "contents2";

        // when
        articleDatabase.insert(Article.of(id1, writer1, title1, contents1));
        articleDatabase.insert(Article.of(id2, writer2, title2, contents2));

        // then
        assertEquals(articleDatabase.selectAll().size(), 2);
        assertTrue(articleDatabase.selectAll().containsKey(id1));
        assertTrue(articleDatabase.selectAll().containsKey(id2));
    }

    @Test
    void 아이디에_맞는_글을_수정한다() {
        // given
        String id = "id";
        String writer = "writer";
        String title = "title";
        String contents = "contents";

        // when
        articleDatabase.insert(Article.of(id, writer, title, contents));
        articleDatabase.update(id, Article.of(id, "newWriter", "newTitle", "newContents"));

        // then
        Article article = articleDatabase.selectById(id);
        assertEquals(article.getWriter(), "newWriter");
        assertEquals(article.getTitle(), "newTitle");
        assertEquals(article.getContents(), "newContents");
    }
}