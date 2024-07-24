package com.woowa.hyeonsik.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.domain.User;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        UserDao userDao = new UserDao();
        userService = new UserService(userDao);
        userDao.clear();
    }

    @Test
    @DisplayName("회원가입을 성공적으로 수행한다.")
    void signup() {
        User user = new User("test", "test", "test", "test@test.test");

        userService.signUp(user);
        final User findedUser = userService.findByUserId(user.getUserId());

        assertThat(findedUser.getUserId()).isEqualTo("test");
        assertThat(findedUser.getName()).isEqualTo("test");
        assertThat(findedUser.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 가입을 시도하면 예외가 발생한다.")
    void signup_exception_when_duplicated_id() {
        User user1 = new User("test", "test1", "test1", "test1@test.test");
        User user2 = new User("test", "test2", "test2", "test2@test.test");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.signUp(user1);
            userService.signUp(user2);
        });
    }

    @ParameterizedTest
    @MethodSource("generateUsers")
    @DisplayName("전체 회원목록을 검색한다.")
    void find_all(List<User> users) {
        for (final User user : users) {
            userService.signUp(user);
        }

        final List<User> all = userService.findAll();

        assertThat(all).hasSize(users.size());
    }

    private static Stream<Arguments> generateUsers() {
        return Stream.of(
            Arguments.of(List.of()),
            Arguments.of(List.of(
                new User("test1", "pass1", "name1", "test1@mail.com"),
                new User("test2", "pass2", "name2", "test2@mail.com"),
                new User("test3", "pass3", "name3", "test3@mail.com")
            ))
        );
    }

    @Test
    @DisplayName("특정 아이디의 회원을 성공적으로 찾는다.")
    void find_by_user_id() {
        User user = new User("test", "test", "test", "test@test.test");
        userService.signUp(user);

        final User findedUser = userService.findByUserId(user.getUserId());

        assertThat(findedUser.getUserId()).isEqualTo("test");
        assertThat(findedUser.getName()).isEqualTo("test");
        assertThat(findedUser.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("특정 아이디의 회원이 없는 경우 예외를 발생시킨다.")
    void find_by_user_id_exception() {
        assertThrows(IllegalArgumentException.class, () -> userService.findByUserId("test"));
    }

}