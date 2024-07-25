package com.woowa.hyeonsik.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.hyeonsik.application.dao.UserDao;
import com.woowa.hyeonsik.application.domain.User;
import java.util.List;
import java.util.stream.Stream;

import com.woowa.hyeonsik.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        userService = new UserService(userDao);
        userDao.clear();
    }

    @Test
    @DisplayName("회원가입을 성공적으로 수행한다.")
    void signup() {
        User user = new User("test", "test", "test", "test@test.test");

        userService.signUp(user);
        final User foundUser = userService.findByUserId(user.getUserId());

        assertThat(foundUser.getUserId()).isEqualTo("test");
        assertThat(foundUser.getName()).isEqualTo("test");
        assertThat(foundUser.getEmail()).isEqualTo("test@test.test");
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

        final User foundUser = userService.findByUserId(user.getUserId());

        assertThat(foundUser.getUserId()).isEqualTo("test");
        assertThat(foundUser.getName()).isEqualTo("test");
        assertThat(foundUser.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("특정 아이디의 회원이 없는 경우 예외를 발생시킨다.")
    void find_by_user_id_exception() {
        assertThrows(IllegalArgumentException.class, () -> userService.findByUserId("test"));
    }

    @Test
    @DisplayName("비밀번호 값이 동일하면 회원 정보 수정을 정상적으로 완료한다.")
    void put_user_success() {
        User user = new User("test", "test", "test", "test@test.test");
        User newUser = new User("test", "test", "hey", "hello@hi.bye");
        userDao.save(user);

        userService.updateUser(newUser);
        User foundUser = userDao.findByUserId("test").get();

        assertThat(foundUser.getName()).isEqualTo("hey");
        assertThat(foundUser.getEmail()).isEqualTo("hello@hi.bye");
    }

    @Test
    @DisplayName("비밀번호 값이 일치하지않으면 회원 수정에서 예외가 발생한다.")
    void put_user_exception_wrong_password() {
        User user = new User("test", "test", "test", "test@test.test");
        User newUser = new User("test", "wrong_password", "hey", "hello@hi.bye");
        userDao.save(user);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(newUser));
    }
}