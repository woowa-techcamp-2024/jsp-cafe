package com.example.db;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.example.entity.Reply;

@DisplayName("ReplyMysqlDatabase 테스트")
class ReplyMysqlDatabaseTest {

	private ReplyMysqlDatabase replyDatabase;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private static MockedStatic<DriverManager> mockedDriverManager;

	@BeforeAll
	static void setUpClass() {
		mockedDriverManager = mockStatic(DriverManager.class);
	}

	@AfterAll
	static void tearDownClass() {
		mockedDriverManager.close();
	}

	@BeforeEach
	void setUp() throws SQLException {
		replyDatabase = new ReplyMysqlDatabase();
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);

		mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
			.thenReturn(connection);
	}

	@Test
	@DisplayName("댓글을 삽입할 수 있다")
	void insert() throws SQLException {
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getLong(1)).thenReturn(1L);

		Reply reply = new Reply(null, "contents", LocalDateTime.now(), false, 1L, "userId", "userName");
		Long replyId = replyDatabase.insert(reply);

		assertThat(replyId).isEqualTo(1L);
		verify(preparedStatement).setString(1, "contents");
		verify(preparedStatement).setTimestamp(2, Timestamp.valueOf(reply.getCreatedAt()));
		verify(preparedStatement).setBoolean(3, false);
		verify(preparedStatement).setLong(4, 1L);
		verify(preparedStatement).setString(5, "userId");
		verify(preparedStatement).setString(6, "userName");
		verify(preparedStatement).executeUpdate();
	}

	@Test
	@DisplayName("ID로 댓글을 찾을 수 있다")
	void findById() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getString("contents")).thenReturn("contents");
		when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
		when(resultSet.getBoolean("deleted")).thenReturn(false);
		when(resultSet.getLong("articleId")).thenReturn(1L);
		when(resultSet.getString("userId")).thenReturn("userId");
		when(resultSet.getString("userName")).thenReturn("userName");

		Optional<Reply> reply = replyDatabase.findById(1L);

		assertThat(reply).isPresent();
		verify(preparedStatement).setLong(1, 1L);
		verify(preparedStatement).executeQuery();
	}

	@Test
	@DisplayName("모든 댓글을 찾을 수 있다")
	void findAll() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, true, false);
		when(resultSet.getLong("id")).thenReturn(1L, 2L);
		when(resultSet.getString("contents")).thenReturn("contents1", "contents2");
		when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
		when(resultSet.getBoolean("deleted")).thenReturn(false);
		when(resultSet.getLong("articleId")).thenReturn(1L);
		when(resultSet.getString("userId")).thenReturn("userId1", "userId2");
		when(resultSet.getString("userName")).thenReturn("userName1", "userName2");

		List<Reply> replies = replyDatabase.findAll();

		assertThat(replies).hasSize(2);
		verify(preparedStatement).executeQuery();
	}

	@Test
	@DisplayName("댓글을 삭제할 수 있다")
	void delete() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		replyDatabase.delete(1L);

		verify(preparedStatement).setBoolean(1, true);
		verify(preparedStatement).setLong(2, 1L);
		verify(preparedStatement).executeUpdate();
	}

	@Test
	@DisplayName("기사 ID로 댓글 수를 세어 반환할 수 있다")
	void countByArticleId() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getLong("count")).thenReturn(2L);

		long count = replyDatabase.countByArticleId(1L);

		assertThat(count).isEqualTo(2L);
		verify(preparedStatement).setLong(1, 1L);
		verify(preparedStatement).executeQuery();
	}

	@Test
	@DisplayName("기사 ID로 댓글을 찾을 수 있다")
	void findByArticleId() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, true, false);
		when(resultSet.getLong("id")).thenReturn(1L, 2L);
		when(resultSet.getString("contents")).thenReturn("contents1", "contents2");
		when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
		when(resultSet.getBoolean("deleted")).thenReturn(false);
		when(resultSet.getString("userId")).thenReturn("userId1", "userId2");
		when(resultSet.getString("userName")).thenReturn("userName1", "userName2");

		List<Reply> replies = replyDatabase.findByArticleId(1L);

		assertThat(replies).hasSize(2);
		verify(preparedStatement).setLong(1, 1L);
		verify(preparedStatement).executeQuery();
	}

	@Test
	@DisplayName("기사 ID로 댓글을 삭제할 수 있다")
	void deleteByArticleId() throws SQLException {
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		replyDatabase.deleteByArticleId(1L);

		verify(preparedStatement).setBoolean(1, true);
		verify(preparedStatement).setLong(2, 1L);
		verify(preparedStatement).executeUpdate();
	}
}
