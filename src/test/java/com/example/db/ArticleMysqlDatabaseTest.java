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

import com.example.entity.Article;

@DisplayName("ArticleMysqlDatabase 테스트")
class ArticleMysqlDatabaseTest {

	private ArticleMysqlDatabase articleDatabase;
	private static MockedStatic<DriverManager> mockedDriverManager;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

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
		articleDatabase = new ArticleMysqlDatabase();
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);

		mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
			.thenReturn(connection);
	}

	@Test
	@DisplayName("아티클을 데이터베이스에 추가할 수 있다")
	void insertArticle() throws SQLException {
		// given
		Article article = new Article(null, "writer", "title", "contents", LocalDateTime.now(), false, "username");
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getGeneratedKeys()).thenReturn(rs);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getLong(1)).thenReturn(1L);

		// when
		articleDatabase.insert(article);

		// then
		verify(preparedStatement).setString(1, article.getUserId());
		verify(preparedStatement).setString(2, article.getTitle());
		verify(preparedStatement).setString(3, article.getContents());
		verify(preparedStatement).executeUpdate();
	}

	@Test
	@DisplayName("아티클 아이디로 아티클을 조회할 수 있다")
	void findById() throws SQLException {
		// given
		Long id = 1L;
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getString("userId")).thenReturn("writer");
		when(resultSet.getString("title")).thenReturn("title");
		when(resultSet.getString("contents")).thenReturn("contents");
		when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

		// when
		Optional<Article> foundArticle = articleDatabase.findById(id);

		// then
		assertThat(foundArticle).isPresent();
		Article article = foundArticle.get();
		assertThat(article.getId()).isEqualTo(id);
		assertThat(article.getUserId()).isEqualTo("writer");
		assertThat(article.getTitle()).isEqualTo("title");
		assertThat(article.getContents()).isEqualTo("contents");
	}

	@Test
	@DisplayName("모든 아티클을 조회할 수 있다")
	void findAll() throws SQLException {
		// given
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, true, false);
		when(resultSet.getLong("id")).thenReturn(1L, 2L);
		when(resultSet.getString("userId")).thenReturn("writer1", "writer2");
		when(resultSet.getString("title")).thenReturn("title1", "title2");
		when(resultSet.getString("contents")).thenReturn("contents1", "contents2");
		when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

		// when
		List<Article> articles = articleDatabase.findAll();

		// then
		assertThat(articles).hasSize(2);
		assertThat(articles).extracting(Article::getId).containsExactly(1L, 2L);
	}

	@Test
	@DisplayName("아티클을 업데이트할 수 있다")
	void updateArticle() throws SQLException {
		// given
		Long id = 1L;
		Article article = new Article(id, "writer", "title", "contents", LocalDateTime.now(), false, "username");
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		// when
		articleDatabase.update(id, article);

		// then
		verify(preparedStatement).setString(1, article.getTitle());
		verify(preparedStatement).setString(2, article.getContents());
		verify(preparedStatement).setLong(4, id);
		verify(preparedStatement).executeUpdate();
	}
}
