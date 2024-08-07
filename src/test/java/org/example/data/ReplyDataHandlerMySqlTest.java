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
import org.example.domain.Reply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReplyDataHandlerMySqlTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private ConnectionProvider mockConnectionProvider;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private ReplyDataHandlerMySql replyDataHandler;

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
        Reply reply = new Reply(null, 1L, 1L, "Test Author", "Test Comment", AliveStatus.ALIVE, LocalDateTime.now());
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        // When
        Reply result = replyDataHandler.insert(reply);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getReplyId());
        verify(mockPreparedStatement).setLong(1, reply.getUserId());
        verify(mockPreparedStatement).setLong(2, reply.getArticleId());
        verify(mockPreparedStatement).setString(3, reply.getAuthor());
        verify(mockPreparedStatement).setString(4, reply.getComment());
        verify(mockPreparedStatement).setString(5, reply.getAliveStatus().name());
        verify(mockPreparedStatement).setTimestamp(eq(6), any(Timestamp.class));
    }

    @Test
    void testUpdate() throws SQLException {
        // Given
        Reply reply = new Reply(1L, 1L, 1L, "Test Author", "Updated Comment", AliveStatus.ALIVE, LocalDateTime.now());
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        Reply result = replyDataHandler.update(reply);

        // Then
        assertNotNull(result);
        assertEquals(reply, result);
        verify(mockPreparedStatement).setString(1, reply.getComment());
        verify(mockPreparedStatement).setString(2, reply.getAliveStatus().name());
        verify(mockPreparedStatement).setLong(3, reply.getReplyId());
    }

    @Test
    void testFindByReplyId() throws SQLException {
        // Given
        Long replyId = 1L;
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("reply_id")).thenReturn(replyId);
        when(mockResultSet.getLong("user_id")).thenReturn(1L);
        when(mockResultSet.getLong("article_id")).thenReturn(1L);
        when(mockResultSet.getString("author")).thenReturn("Test Author");
        when(mockResultSet.getString("comment")).thenReturn("Test Comment");
        when(mockResultSet.getString("alive_status")).thenReturn(AliveStatus.ALIVE.name());
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        Reply result = replyDataHandler.findByReplyId(replyId);

        // Then
        assertNotNull(result);
        assertEquals(replyId, result.getReplyId());
        verify(mockPreparedStatement).setLong(1, replyId);
    }

    @Test
    void testFindAllByArticleId() throws SQLException {
        // Given
        Long articleId = 1L;
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("reply_id")).thenReturn(1L, 2L);
        when(mockResultSet.getLong("user_id")).thenReturn(1L, 2L);
        when(mockResultSet.getLong("article_id")).thenReturn(articleId, articleId);
        when(mockResultSet.getString("author")).thenReturn("Author 1", "Author 2");
        when(mockResultSet.getString("comment")).thenReturn("Comment 1", "Comment 2");
        when(mockResultSet.getString("alive_status")).thenReturn(AliveStatus.ALIVE.name());
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        List<Reply> results = replyDataHandler.findAllByArticleId(articleId);

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(mockPreparedStatement).setString(1, AliveStatus.ALIVE.name());
        verify(mockPreparedStatement).setLong(2, articleId);
    }

    @Test
    void testDeleteAllByArticleId() throws SQLException {
        // Given
        Long articleId = 1L;
        when(mockPreparedStatement.executeUpdate()).thenReturn(2); // Assuming 2 replies were deleted

        // When
        replyDataHandler.deleteAllByArticleId(articleId);

        // Then
        verify(mockPreparedStatement).setString(1, AliveStatus.DELETED.name());
        verify(mockPreparedStatement).setLong(2, articleId);
        verify(mockPreparedStatement).executeUpdate();
    }
}