package com.jspcafe.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void User_객체가_올바르게_생성된다() {
        // Given
        String email = "woowa@woowa.in";
        String nickName = "김배달";
        String password = "비밀번호";

        // When
        User user = User.create(email, nickName, password);

        // Then
        assertNotNull(user);
        assertEquals(user.email(), "woowa@woowa.in");
        assertEquals(user.nickname(), "김배달");
        assertTrue(user.verifyPassword("비밀번호"));
    }

    @Test
    void User_올바르지_않은_이메일이면_예외를_발생시킨다() {
        // Given
        String email = "";
        String nickName = "김배달";
        String password = "비밀번호";

        // When Then
        assertThrows(IllegalArgumentException.class, () -> User.create(email, nickName, password));
    }

    @Test
    void User_올바르지_않은_닉네임이면_예외를_발생시킨다() {
        // Given
        String email = "woowa@woowa.in";
        String nickName = "";
        String password = "비밀번호";

        // When Then
        assertThrows(IllegalArgumentException.class, () -> User.create(email, nickName, password));
    }

    @Test
    void User_올바르지_않은_비밀번호이면_예외를_발생시킨다() {
        // Given
        String email = "woowa@woowa.in";
        String nickName = "김배달";
        String password = "";

        // When Then
        assertThrows(IllegalArgumentException.class, () -> User.create(email, nickName, password));
    }
}
