package cafe.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    @DisplayName("Article 객체가 올바르게 생성되는지 테스트")
    public void testArticleCreation() {
        Article article = Article.of("author1", "Test Title", "Test Contents");
        assertNotNull(article);
        assertEquals("author1", article.getWriter());
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Contents", article.getContents());
        assertNotNull(article.getCreated());
    }

    @Test
    @DisplayName("작성자가 null이거나 빈 값일 때 예외를 던지는지 테스트")
    public void testInvalidWriter() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of("", "Test Title", "Test Contents");
        });
        assertEquals("Writer is empty!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of(null, "Test Title", "Test Contents");
        });
        assertEquals("Writer is empty!", exception.getMessage());
    }

    @Test
    @DisplayName("제목이 null이거나 빈 값일 때 예외를 던지는지 테스트")
    public void testInvalidTitle() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of("author1", "", "Test Contents");
        });
        assertEquals("Title is empty!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of("author1", null, "Test Contents");
        });
        assertEquals("Title is empty!", exception.getMessage());
    }

    @Test
    @DisplayName("내용이 null이거나 빈 값일 때 예외를 던지는지 테스트")
    public void testInvalidContents() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of("author1", "Test Title", "");
        });
        assertEquals("Contents is empty!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            Article.of("author1", "Test Title", null);
        });
        assertEquals("Contents is empty!", exception.getMessage());
    }
}