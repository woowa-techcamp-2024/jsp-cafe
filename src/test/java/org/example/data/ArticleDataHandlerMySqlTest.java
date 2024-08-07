package org.example.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.example.constance.AliveStatus;
import org.example.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ArticleDataHandlerMySqlTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private ConnectionProvider mockConnectionProvider;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private ArticleDataHandlerMySql articleDataHandler;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnectionProvider.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    void testInsert() throws SQLException {
        // Given
        Article article = new Article(null, "Test Title", "Test Content", "Test Author",
                LocalDateTime.now(), AliveStatus.ALIVE, 1L);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        // When
        Article result = articleDataHandler.insert(article);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getArticleId());
        verify(mockPreparedStatement).setString(1, article.getTitle());
        verify(mockPreparedStatement).setString(2, article.getContent());
        verify(mockPreparedStatement).setString(3, article.getAuthor());
        verify(mockPreparedStatement).setTimestamp(eq(4), any(Timestamp.class));
        verify(mockPreparedStatement).setString(5, article.getAlivestatus().name());
        verify(mockPreparedStatement).setLong(6, article.getUserId());
    }

    @Test
    void testUpdate() throws SQLException {
        // Given
        Article article = new Article(1L, "Updated Title", "Updated Content", "Updated Author",
                LocalDateTime.now(), AliveStatus.ALIVE, 1L);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        Article result = articleDataHandler.update(article);

        // Then
        assertNotNull(result);
        assertEquals(article, result);
        verify(mockPreparedStatement).setString(1, article.getTitle());
        verify(mockPreparedStatement).setString(2, article.getContent());
        verify(mockPreparedStatement).setString(3, article.getAuthor());
        verify(mockPreparedStatement).setTimestamp(eq(4), any(Timestamp.class));
        verify(mockPreparedStatement).setString(5, article.getAlivestatus().name());
        verify(mockPreparedStatement).setLong(6, article.getUserId());
        verify(mockPreparedStatement).setLong(7, article.getArticleId());
    }

    @Test
    void testFindByArticleId() throws SQLException {
        // Given
        Long articleId = 1L;
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("article_id")).thenReturn(articleId);
        when(mockResultSet.getString("title")).thenReturn("Test Title");
        when(mockResultSet.getString("content")).thenReturn("Test Content");
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getString("alive_status")).thenReturn(AliveStatus.ALIVE.name());
        when(mockResultSet.getLong("user_id")).thenReturn(1L);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        Article result = articleDataHandler.findByArticleId(articleId);

        // Then
        assertNotNull(result);
        assertEquals(articleId, result.getArticleId());
        verify(mockPreparedStatement).setLong(1, articleId);
    }

    @Test
    void testFindAll() throws SQLException {
        // Given
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("article_id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("title")).thenReturn("Title 1", "Title 2");
        when(mockResultSet.getString("content")).thenReturn("Content 1", "Content 2");
        when(mockResultSet.getString("author")).thenReturn("Author 1", "Author 2");
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getString("alive_status")).thenReturn(AliveStatus.ALIVE.name());
        when(mockResultSet.getLong("user_id")).thenReturn(1L, 2L);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        List<Article> results = articleDataHandler.findAll();

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(mockPreparedStatement).setString(1, AliveStatus.ALIVE.name());
    }

    @Test
    void testGetTotalPageNumber() throws SQLException {
        // Given
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("total")).thenReturn(30); // Assuming 30 articles
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        int result = articleDataHandler.getTotalPageNumber();

        // Then
        assertEquals(2, result); // 30 articles / 15 per page = 2 pages
        verify(mockPreparedStatement).setString(1, AliveStatus.ALIVE.name());
    }
}