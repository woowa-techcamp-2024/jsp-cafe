package org.example.entity;

import org.example.entity.Article;
import org.example.entity.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ArticleTest {

    @Mock
    private Reply reply1;

    @Mock
    private Reply reply2;

    private Article article;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        article = new Article(1, "Test Title", "Test Content", "author1");
    }

    @Test
    public void testArticleCreationWithId() {
        Article article = new Article(1, "Test Title", "Test Content", "author1");

        assertEquals(1, article.getArticleId());
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Content", article.getContent());
        assertEquals("author1", article.getAuthor());
    }

    @Test
    public void testArticleCreationWithoutId() {
        Article article = new Article("Test Title", "Test Content", "author1");

        assertNull(article.getArticleId());
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Content", article.getContent());
        assertEquals("author1", article.getAuthor());
    }

    @Test
    public void testSetArticleId() {
        Article article = new Article("Test Title", "Test Content", "author1");
        article.setArticleId(2);

        assertEquals(2, article.getArticleId());
    }

    @Test
    public void testIsOwner() {
        assertTrue(article.isOwner("author1"));
        assertFalse(article.isOwner("author2"));
    }

    @Test
    public void testReplyAll() {
        when(reply1.isOwner("author1")).thenReturn(true);
        when(reply2.isOwner("author1")).thenReturn(true);

        List<Reply> replies = List.of(reply1, reply2);
        assertTrue(article.replyAll(replies));

        when(reply2.isOwner("author1")).thenReturn(false);
        assertFalse(article.replyAll(replies));
    }
}
