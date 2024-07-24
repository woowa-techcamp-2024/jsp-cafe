package com.example.db;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.entity.User;

public class UserMemoryDatabaseTest {

	private UserMemoryDatabase userMemoryDatabase;

	@BeforeEach
	void setUp() {
		userMemoryDatabase = new UserMemoryDatabase();
	}

	@DisplayName("같은 userId가 동시에 접근하면 실패한다.")
	@Test
	void should_fail_when_user_id_is_same() throws Exception {
		int threadNum = 10;
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

		User user = new User("a", "b", "c", "d");

		AtomicInteger fail = new AtomicInteger(0);
		for (int i = 0; i < threadNum; i++) {
			executorService.execute(() -> {
				try {
					userMemoryDatabase.insert(user);
				} catch (Exception e) {
					fail.getAndIncrement();
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		executorService.shutdown();

		assertThat(fail.get()).isEqualTo(threadNum - 1);
	}

	@Test
	@DisplayName("유저를 데이터베이스에 추가할 수 있다")
	void insertUser() {
		// given
		UserMemoryDatabase userMemoryDatabase = new UserMemoryDatabase();
		User user = new User("1", "password", "name", "email@example.com");

		// when
		userMemoryDatabase.insert(user);

		// then
		Optional<User> foundUser = userMemoryDatabase.findById("1");
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().id()).isEqualTo("1");
		assertThat(foundUser.get().name()).isEqualTo("name");
		assertThat(foundUser.get().email()).isEqualTo("email@example.com");
	}

	@Test
	@DisplayName("모든 유저를 조회할 수 있다")
	void findAllUsers() {
		// given
		UserMemoryDatabase userMemoryDatabase = new UserMemoryDatabase();
		User user1 = new User("1", "password", "name1", "email1@example.com");
		User user2 = new User("2", "password", "name2", "email2@example.com");
		userMemoryDatabase.insert(user1);
		userMemoryDatabase.insert(user2);

		// when
		List<User> users = userMemoryDatabase.findAll();

		// then
		assertThat(users).hasSize(2).containsExactlyInAnyOrder(user1, user2);
	}

	@ParameterizedTest(name = "유저 아이디 {0}로 유저를 조회할 수 있다")
	@ValueSource(strings = {"1", "2"})
	@DisplayName("유저 아이디로 유저를 조회할 수 있다")
	void findUserById(String userId) {
		// given
		UserMemoryDatabase userMemoryDatabase = new UserMemoryDatabase();
		User user1 = new User("1", "password", "name1", "email1@example.com");
		User user2 = new User("2", "password", "name2", "email2@example.com");
		userMemoryDatabase.insert(user1);
		userMemoryDatabase.insert(user2);

		// when
		Optional<User> foundUser = userMemoryDatabase.findById(userId);

		// then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().id()).isEqualTo(userId);
	}

	@Test
	@DisplayName("존재하지 않는 유저 아이디로 유저를 조회하면 빈 값을 반환한다")
	void findUserByIdNotFound() {
		// given
		UserMemoryDatabase userMemoryDatabase = new UserMemoryDatabase();

		// when
		Optional<User> foundUser = userMemoryDatabase.findById("nonexistent");

		// then
		assertThat(foundUser).isNotPresent();
	}

	@Test
	@DisplayName("유저를 업데이트할 수 있다")
	void updateUser() {
		UserMemoryDatabase userMemoryDatabase = new UserMemoryDatabase();
		User user = new User("1", "password", "name", "email@example.com");
		userMemoryDatabase.insert(user);

		User updatedUser = new User("1", "newPassword", "newName", "newEmail@example.com");
		userMemoryDatabase.update("1", updatedUser);

		Optional<User> foundUser = userMemoryDatabase.findById("1");
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().password()).isEqualTo("newPassword");
		assertThat(foundUser.get().name()).isEqualTo("newName");
		assertThat(foundUser.get().email()).isEqualTo("newEmail@example.com");
	}
}
