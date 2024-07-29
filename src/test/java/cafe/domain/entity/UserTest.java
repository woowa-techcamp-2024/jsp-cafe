package cafe.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("User 객체가 올바르게 생성되는지 테스트")
    public void testUserCreation() {
        User user = User.of("user1", "password123", "John Doe", "john.doe@example.com");
        assertNotNull(user);
        assertEquals("user1", user.getUserid());
    }

    @Test
    @DisplayName("ID가 null이거나 빈 값일 때 예외를 던지는지 테스트")
    public void testInvalidId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            User.of("", "password123", "John Doe", "john.doe@example.com");
        });
        assertEquals("Id is empty!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            User.of(null, "password123", "John Doe", "john.doe@example.com");
        });
        assertEquals("Id is empty!", exception.getMessage());
    }

    @Test
    @DisplayName("이메일이 null이거나 빈 값일 때 예외를 던지는지 테스트")
    public void testInvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            User.of("user1", "password123", "John Doe", "");
        });
        assertEquals("Email is empty!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            User.of("user1", "password123", "John Doe", null);
        });
        assertEquals("Email is empty!", exception.getMessage());
    }
}