package com.example.db;

import com.example.entity.User;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("UserMysqlDatabase 테스트")
class UserMysqlDatabaseTest {

	private UserMysqlDatabase userDatabase;
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
		userDatabase = new UserMysqlDatabase();
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);

		mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
			.thenReturn(connection);
	}

	@Test
	@DisplayName("유저를 데이터베이스에 추가할 수 있다")
	void insertUser() throws SQLException {
		// given
		User user = new User("1", "password", "name", "email@example.com");
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		// when
		userDatabase.insert(user);

		// then
		verify(preparedStatement).setString(1, user.id());
		verify(preparedStatement).setString(2, user.password());
		verify(preparedStatement).setString(3, user.name());
		verify(preparedStatement).setString(4, user.email());
		verify(preparedStatement).executeUpdate();
	}

	@Test
	@DisplayName("유저 아이디로 유저를 조회할 수 있다")
	void findById() throws SQLException {
		// given
		String id = "1";
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getString("id")).thenReturn(id);
		when(resultSet.getString("password")).thenReturn("password");
		when(resultSet.getString("name")).thenReturn("name");
		when(resultSet.getString("email")).thenReturn("email@example.com");

		// when
		Optional<User> foundUser = userDatabase.findById(id);

		// then
		assertThat(foundUser).isPresent();
		User user = foundUser.get();
		assertThat(user.id()).isEqualTo(id);
		assertThat(user.password()).isEqualTo("password");
		assertThat(user.name()).isEqualTo("name");
		assertThat(user.email()).isEqualTo("email@example.com");
	}

	@Test
	@DisplayName("모든 유저를 조회할 수 있다")
	void findAll() throws SQLException {
		// given
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true, true, false);
		when(resultSet.getString("id")).thenReturn("1", "2");
		when(resultSet.getString("password")).thenReturn("password1", "password2");
		when(resultSet.getString("name")).thenReturn("name1", "name2");
		when(resultSet.getString("email")).thenReturn("email1@example.com", "email2@example.com");

		// when
		List<User> users = userDatabase.findAll();

		// then
		assertThat(users).hasSize(2);
		assertThat(users).extracting(User::id).containsExactly("1", "2");
	}

	@Test
	@DisplayName("유저를 업데이트할 수 있다")
	void updateUser() throws SQLException {
		// given
		String id = "1";
		User user = new User(id, "password", "name", "email@example.com");
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		// when
		userDatabase.update(id, user);

		// then
		verify(preparedStatement).setString(1, user.name());
		verify(preparedStatement).setString(2, user.email());
		verify(preparedStatement).setString(3, id);
		verify(preparedStatement).executeUpdate();
	}
}
