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
import org.example.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserDataHandlerMySqlTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private ConnectionProvider mockConnectionProvider;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private UserDataHandlerMySql userDataHandler;

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
        User user = new User(null, "test@test.com", "testuser", "password", LocalDateTime.now());
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        // When
        User result = userDataHandler.insert(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        verify(mockPreparedStatement).setString(1, user.getEmail());
        verify(mockPreparedStatement).setString(2, user.getNickname());
        verify(mockPreparedStatement).setString(3, user.getPassword());
        verify(mockPreparedStatement).setTimestamp(eq(4), any(Timestamp.class));
    }

    @Test
    void testUpdate() throws SQLException {
        // Given
        User user = new User(1L, "update@test.com", "updateuser", "newpassword", LocalDateTime.now());
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // When
        User result = userDataHandler.update(user);

        // Then
        assertNotNull(result);
        assertEquals(user, result);
        verify(mockPreparedStatement).setString(1, user.getEmail());
        verify(mockPreparedStatement).setString(2, user.getNickname());
        verify(mockPreparedStatement).setString(3, user.getPassword());
        verify(mockPreparedStatement).setTimestamp(eq(4), any(Timestamp.class));
        verify(mockPreparedStatement).setLong(5, user.getUserId());
    }

    @Test
    void testFindByUserId() throws SQLException {
        // Given
        Long userId = 1L;
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("user_id")).thenReturn(userId);
        when(mockResultSet.getString("email")).thenReturn("test@test.com");
        when(mockResultSet.getString("nickname")).thenReturn("testuser");
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        User result = userDataHandler.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(mockPreparedStatement).setLong(1, userId);
    }

    @Test
    void testFindByEmail() throws SQLException {
        // Given
        String email = "test@test.com";
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("user_id")).thenReturn(1L);
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getString("nickname")).thenReturn("testuser");
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        User result = userDataHandler.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(mockPreparedStatement).setString(1, email);
    }

    @Test
    void testFindAll() throws SQLException {
        // Given
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("user_id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("email")).thenReturn("user1@test.com", "user2@test.com");
        when(mockResultSet.getString("nickname")).thenReturn("user1", "user2");
        when(mockResultSet.getString("password")).thenReturn("password1", "password2");
        when(mockResultSet.getTimestamp("created_dt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // When
        List<User> results = userDataHandler.findAll();

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
    }
}