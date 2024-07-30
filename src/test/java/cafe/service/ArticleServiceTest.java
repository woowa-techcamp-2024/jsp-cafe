package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.H2Connector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {
    private static ArticleDatabase articleDatabase;
    private static ArticleService articleService;

    @BeforeAll
    static void setUp() {
        DatabaseConnector databaseConnector = new H2Connector();
        articleDatabase = new ArticleDatabase(databaseConnector);
        articleService = new ArticleService(articleDatabase);
    }

    @AfterEach
    void tearDown() {
        articleDatabase.deleteAll();
    }

    @Test
    void 올바른_글을_저장한다() {
        // given
        String articleId = "id";
        String writer = "writer";
        String title = "title";
        String contents = "contents";

        // when
        articleService.save(articleId, writer, title, contents);

        // then
        Article article = articleService.find(articleId);
        assertEquals(writer, article.getWriter());
        assertEquals(title, article.getTitle());
        assertEquals(contents, article.getContents());
    }

    @Test
    void 경로의_아이디로_글을_조회한다() {
        // given
        String articleId = "id";
        String writer = "writer";
        String title = "title";
        String contents = "contents";
        articleService.save(articleId, writer, title, contents);

        // when
        String uri = "/articles/id";
        String id = uri.split("/")[2];
        var article = articleService.find(id);

        // then
        assertEquals(writer, article.getWriter());
        assertEquals(title, article.getTitle());
        assertEquals(contents, article.getContents());
    }

    @Test
    void 아이디에_해당하는_글이_없다면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> articleService.find("id"));
    }

    @Test
    void 전체_글을_조회한다() {
        // given
        String writer1 = "writer1";
        String title1 = "title1";
        String contents1 = "contents1";
        articleService.save(writer1, title1, contents1);

        String writer2 = "writer2";
        String title2 = "title2";
        String contents2 = "contents2";
        articleService.save(writer2, title2, contents2);

        // when
        var articles = articleService.findAll();

        // then
        assertEquals(2, articles.size());
    }
}