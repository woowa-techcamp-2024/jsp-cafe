package com.example.service;

import com.example.db.ArticleDatabase;
import com.example.db.ReplyDatabase;
import com.example.db.UserDatabase;
import com.example.dto.LoginRequest;
import com.example.dto.SignupRequest;
import com.example.dto.UserUpdateRequest;
import com.example.entity.User;
import com.example.exception.BaseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("UserService 테스트")
class UserServiceTest {

	private UserService userService;
	private UserDatabase userDatabase;
	private ArticleDatabase articleDatabase;
	private ReplyDatabase replyDatabase;

	@BeforeEach
	void setUp() {
		userDatabase = mock(UserDatabase.class);
		articleDatabase = mock(ArticleDatabase.class);
		replyDatabase = mock(ReplyDatabase.class);
		userService = new UserService(userDatabase, articleDatabase, replyDatabase);
	}

	@Test
	@DisplayName("회원가입을 할 수 있다")
	void signup() {
		// given
		SignupRequest request = new SignupRequest("user1", "password", "name", "email@example.com");
		when(userDatabase.findById(request.id())).thenReturn(Optional.empty());

		// when
		userService.signup(request);

		// then
		verify(userDatabase, times(1)).insert(any(User.class));
	}

	@Test
	@DisplayName("중복된 아이디로 회원가입 시 예외를 던진다")
	void signupDuplicateId() {
		// given
		SignupRequest request = new SignupRequest("user1", "password", "name", "email@example.com");
		when(userDatabase.findById(request.id())).thenReturn(Optional.of(mock(User.class)));

		// when & then
		assertThatThrownBy(() -> userService.signup(request))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("user already exists");
	}

	@Test
	@DisplayName("로그인할 수 있다")
	void login() {
		// given
		LoginRequest request = new LoginRequest("user1", "password");
		User user = new User("user1", "password", "name", "email@example.com");
		when(userDatabase.findById(request.id())).thenReturn(Optional.of(user));

		// when
		User loggedInUser = userService.login(request);

		// then
		assertThat(loggedInUser).isEqualTo(user);
	}

	@Test
	@DisplayName("잘못된 아이디로 로그인 시 예외를 던진다")
	void loginInvalidId() {
		// given
		LoginRequest request = new LoginRequest("user1", "password");
		when(userDatabase.findById(request.id())).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.login(request))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("invalid auth");
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인 시 예외를 던진다")
	void loginInvalidPassword() {
		// given
		LoginRequest request = new LoginRequest("user1", "password");
		User user = new User("user1", "wrongPassword", "name", "email@example.com");
		when(userDatabase.findById(request.id())).thenReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> userService.login(request))
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("invalid auth");
	}

	@Test
	@DisplayName("유저 정보를 조회할 수 있다")
	void getUser() {
		// given
		String userId = "user1";
		User user = new User(userId, "password", "name", "email@example.com");
		when(userDatabase.findById(userId)).thenReturn(Optional.of(user));

		// when
		User foundUser = userService.getUser(userId);

		// then
		assertThat(foundUser).isEqualTo(user);
	}

	@Test
	@DisplayName("존재하지 않는 유저 조회 시 예외를 던진다")
	void getUserNotFound() {
		// given
		String userId = "user1";
		when(userDatabase.findById(userId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.getUser(userId))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("user not found");
	}

	@Test
	@DisplayName("유저 정보를 업데이트할 수 있다")
	void updateUser() {
		// given
		String userId = "user1";
		User user = new User(userId, "password", "name", "email@example.com");
		UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail@example.com", "password");
		when(userDatabase.findById(userId)).thenReturn(Optional.of(user));

		// when
		userService.updateUser(userId, request);

		// then
		verify(userDatabase, times(1)).update(eq(userId), any(User.class));
	}

	@Test
	@DisplayName("존재하지 않는 유저 업데이트 시 예외를 던진다")
	void updateUserNotFound() {
		// given
		String userId = "user1";
		UserUpdateRequest request = new UserUpdateRequest("password", "newName", "newEmail@example.com");
		when(userDatabase.findById(userId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> userService.updateUser(userId, request))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("user not found");
	}

	@Test
	@DisplayName("유저 아이디와 비밀번호가 일치하지 않으면 업데이트 시 예외를 던진다")
	void updateUserInvalidPassword() {
		// given
		String userId = "user1";
		User user = new User(userId, "password", "name", "email@example.com");
		UserUpdateRequest request = new UserUpdateRequest("wrongPassword", "newName", "newEmail@example.com");
		when(userDatabase.findById(userId)).thenReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> userService.updateUser(userId, request))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("invalid password");
	}
}
