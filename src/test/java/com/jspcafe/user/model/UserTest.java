package com.jspcafe.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

  @Test
  void User의_정보를_업데이트한다() {
    // Given
    User user = User.create("woowa@woowa.in", "김배달", "1234");

    // When
    String updateEmail = "wowa@wa.in";
    String updateNickname = "이배민";
    String updatePassword = "4321";
    User updateUser = user.update(updateEmail, updateNickname, updatePassword);

    // Then
    assertEquals(user.id(), updateUser.id());
    assertEquals(updateEmail, updateUser.email());
    assertEquals(updateNickname, updateUser.nickname());
    assertTrue(updateUser.verifyPassword("4321"));
  }
}
