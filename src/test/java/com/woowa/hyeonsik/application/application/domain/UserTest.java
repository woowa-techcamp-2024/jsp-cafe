package com.woowa.hyeonsik.application.application.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import com.woowa.hyeonsik.application.domain.User;
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
    void throw_when_least_getPassword() {
        String userId = "testUser";
        String password = "t";
        String name = "testName";
        String email = "test@mail.com";

        assertThrows(IllegalArgumentException.class, () -> new User(userId, password, name, email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "javajigi.com", "javajigi", "javajigi@com", "javajigi@@@", "javajigi@@@.com", "javajigi@@@naver.com"})
    @DisplayName("이메일 규격에 맞지 않으면 예외가 발생한다.")
    void throw_when_getEmail(String email) {
        String userId = "testUser";
        String password = "testPassword";
        String name = "testName";

        assertThrows(IllegalArgumentException.class, () -> new User(userId, password, name, email));
    }

    @Test
    @DisplayName("동일한 User 객체는 equals가 true를 반환해야 한다")
    void givenTwoIdenticalUsers_whenEquals_thenReturnTrue() {
        // given
        User user1 = new User("user1", "password", "name", "email@example.com");
        User user2 = new User("user1", "password", "name", "email@example.com");

        // when
        boolean isEqual = user1.equals(user2);

        // then
        assertTrue(isEqual, "동일한 User 객체는 equals가 true를 반환해야 합니다.");
    }

    @Test
    @DisplayName("서로 다른 User 객체는 equals가 false를 반환해야 한다")
    void givenTwoDifferentUsers_whenEquals_thenReturnFalse() {
        // given
        User user1 = new User("user1", "password1", "name1", "email1@example.com");
        User user2 = new User("user2", "password2", "name2", "email2@example.com");

        // when
        boolean isEqual = user1.equals(user2);

        // then
        assertFalse(isEqual, "서로 다른 User 객체는 equals가 false를 반환해야 합니다.");
    }

    @ParameterizedTest
    @MethodSource("provideUsersForHashCodeTest")
    @DisplayName("동일한 User 객체는 동일한 hashCode를 가져야 한다")
    void givenTwoIdenticalUsers_whenHashCode_thenReturnSameHashCode(User user1, User user2, boolean expected) {
        // when
        int hashCode1 = user1.hashCode();
        int hashCode2 = user2.hashCode();

        // then
        if (expected) {
            assertEquals(hashCode1, hashCode2, "동일한 User 객체는 동일한 hashCode를 가져야 합니다.");
        } else {
            assertNotEquals(hashCode1, hashCode2, "서로 다른 User 객체는 다른 hashCode를 가져야 합니다.");
        }
    }

    private static Stream<Arguments> provideUsersForHashCodeTest() {
        return Stream.of(
            Arguments.of(new User("user1", "password", "name", "email@example.com"),
                new User("user1", "password", "name", "email@example.com"), true),
            Arguments.of(new User("user1", "password1", "name1", "email1@example.com"),
                new User("user2", "password2", "name2", "email2@example.com"), false)
        );
    }

    @Test
    @DisplayName("User 객체는 자기 자신과 equals 비교 시 true를 반환해야 한다")
    void givenUser_whenEqualsItself_thenReturnTrue() {
        // given
        User user = new User("user1", "password", "name", "email@example.com");

        // when
        boolean isEqual = user.equals(user);

        // then
        assertTrue(isEqual, "User 객체는 자기 자신과 equals 비교 시 true를 반환해야 합니다.");
    }

    @Test
    @DisplayName("User 객체는 null과 equals 비교 시 false를 반환해야 한다")
    void givenUser_whenEqualsNull_thenReturnFalse() {
        // given
        User user = new User("user1", "password", "name", "email@example.com");

        // when
        boolean isEqual = user.equals(null);

        // then
        assertFalse(isEqual, "User 객체는 null과 equals 비교 시 false를 반환해야 합니다.");
    }
}
