package org.example.member.model.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    @DisplayName("유효한 정보로 사용자를 생성할 수 있다.")
    public void create_user_with_valid_inputs() {
        // Given
        String userId = "user123";
        String password = "password";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When
        User user = User.createUser(userId, password, name, email);

        // Then
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("정상적인 이메일 형식이면 예외가 발생하지 않는다.")
    public void validate_user_with_correct_email_format() {
        // Given
        String userId = "user123";
        String password = "password";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When & Then
        assertDoesNotThrow(() -> {
            User.createUser(userId, password, name, email);
        });
    }

    @Test
    @DisplayName("get을 통해 사용자 정보를 불러올 수 있다.")
    public void retrieving_user_details_correctly() {
        // Given
        String userId = "user123";
        String password = "password";
        String name = "John Doe";
        String email = "john.doe@example.com";
        User user = User.createUser(userId, password, name, email);

        // When
        String retrievedUserId = user.getUserId();
        String retrievedPassword = user.getPassword();
        String retrievedName = user.getName();
        String retrievedEmail = user.getEmail();

        // Then
        assertEquals(userId, retrievedUserId);
        assertEquals(password, retrievedPassword);
        assertEquals(name, retrievedName);
        assertEquals(email, retrievedEmail);
    }

    @Test
    @DisplayName("toString이 정상적으로 동작한다.")
    public void test_toString_method_correct_format() {
        // Given
        User user = User.createUser("user123", "password", "John Doe", "john.doe@example.com");

        // When
        String result = user.toString();

        // Then
        assertEquals("Member{userId='user123', password='password', name='John Doe', email='john.doe@example.com'}",
                result);
    }

    // Creating a user with an empty userId should throw an IllegalArgumentException
    @Test
    @DisplayName("userId가 비어있으면 IllegalArgumentException이 발생한다.")
    public void create_user_with_empty_userid() {
        // Given
        String userId = "";
        String password = "password";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(userId, password, name, email);
        });
    }

    // Creating a user with an empty password should throw an IllegalArgumentException
    @Test
    @DisplayName("비밀번호가 비어있으면 IllegalArgumentException 발생한다.")
    public void create_user_with_empty_password() {
        // Given
        String userId = "user123";
        String password = "";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(userId, password, name, email);
        });
    }

    // Creating a user with an empty name should throw an IllegalArgumentException
    @Test
    @DisplayName("이름이 비어있다면 IllegalArgumentException이 발생한다.")
    public void create_user_with_empty_name() {
        // Given
        String userId = "user123";
        String password = "password";
        String name = "";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(userId, password, name, email);
        });
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않다면 IllegalArgumentException이 발생한다.")
    public void creating_user_with_invalid_email_format_should_throw_exception() {
        // Given
        String invalidEmail = "invalid_email_format";

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> User.createUser("user123", "password", "John Doe", invalidEmail));
    }

    @Test
    @DisplayName("필드에 null이 포함돼 있다면 IllegalArgumentException이 발생한다.")
    public void creating_user_with_null_values_should_throw_exception() {
        // Given
        String userId = null;
        String password = "password";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            User.createUser(userId, password, name, email);
        });
    }

}