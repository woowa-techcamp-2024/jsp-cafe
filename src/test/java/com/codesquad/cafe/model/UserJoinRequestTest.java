package com.codesquad.cafe.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codesquad.cafe.exception.ValidationException;
import org.junit.jupiter.api.Test;

class UserJoinRequestTest {

    @Test
    void testValidUserJoinRequest() {
        UserJoinRequest request = new UserJoinRequest("testuser", "password123", "Test User", "testuser@example.com");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void testInvalidUserJoinRequest_MissingFields() {
        UserJoinRequest request = new UserJoinRequest("", "", "", "");
        ValidationException exception = assertThrows(ValidationException.class, request::validate);
        String expectedMessage = "username은 필수입니다.\npassword는 필수입니다.\nname은 필수입니다.\nemail은 필수입니다.\n";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidUserJoinRequest_InvalidEmailFormat() {
        UserJoinRequest request = new UserJoinRequest("testuser", "password123", "Test User", "invalid-email");
        ValidationException exception = assertThrows(ValidationException.class, request::validate);
        String expectedMessage = "잘못된 email 형식입니다.\n";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
