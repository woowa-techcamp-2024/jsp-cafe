package com.woowa.hyeonsik.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {
    @Test
    @DisplayName("성공적으로 유저를 생성한다.")
    void create_user() {
        String userId = "testUser";
        String password = "testPassword";
        String name = "testName";
        String email = "test@mail.com";

        final User user = new User(userId, password, name, email);

        assertThat(user.getUserId()).isEqualTo("testUser");
        assertThat(user.getPassword()).isEqualTo("testPassword");
        assertThat(user.getName()).isEqualTo("testName");
        assertThat(user.getEmail()).isEqualTo("test@mail.com");
    }

    @ParameterizedTest
    @MethodSource("generateUserData")
    @DisplayName("UserId, Password, Name, Email이 빈 값이면 예외가 발생한다.")
    void throw_when_null(String userId, String password, String name, String email) {
        assertThrows(IllegalArgumentException.class, () -> new User(userId, password, name, email));
    }

    private static Stream<Arguments> generateUserData() {
        return Stream.of(
            Arguments.of(null, "testPassword", "testName", "test@mail.com"),
            Arguments.of("", "testPassword", "testName", "test@mail.com"),
            Arguments.of("testUser", null, "testName", "test@mail.com"),
            Arguments.of("testUser", "testPassword", null, "test@mail.com"),
            Arguments.of("testUser", "testPassword", "", "test@mail.com"),
            Arguments.of("testUser", "testPassword", "testName", null)
        );
    }

    @Test
    @DisplayName("암호가 2글자 보다 작으면 예외가 발생한다.")
    void throw_when_least_password() {
        String userId = "testUser";
        String password = "t";
        String name = "testName";
        String email = "test@mail.com";

        assertThrows(IllegalArgumentException.class, () -> new User(userId, password, name, email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "javajigi.com", "javajigi", "javajigi@com", "javajigi@@@", "javajigi@@@.com", "javajigi@@@naver.com"})
    @DisplayName("이메일 규격에 맞지 않으면 예외가 발생한다.")
    void throw_when_email(String email) {
        String userId = "testUser";
        String password = "testPassword";
        String name = "testName";

        assertThrows(IllegalArgumentException.class, () -> new User(userId, password, name, email));
    }
}
