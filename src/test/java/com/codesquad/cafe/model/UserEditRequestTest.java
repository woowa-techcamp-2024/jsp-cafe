package com.codesquad.cafe.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codesquad.cafe.exception.ValidationException;
import org.junit.jupiter.api.Test;

class UserEditRequestTest {

    @Test
    void testValidUserEditRequest() {
        UserEditRequest request = new UserEditRequest(1L, "testuser", "password123", "password123", "password123", "Test User", "testuser@example.com");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void testInvalidUserEditRequest_MissingFields() {
        UserEditRequest request = new UserEditRequest(null, "", "", "", "", "", "");
        ValidationException exception = assertThrows(ValidationException.class, request::validate);
        String expectedMessage = "id는 1이상의 자연수여야 합니다.\nusername은 필수입니다.\noriginalPassword는 필수입니다.\npassword는 필수입니다.\nconfirmPassword는 필수입니다.\nname은 필수입니다.\nemail은 필수입니다.\n";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidUserEditRequest_InvalidEmailFormat() {
        UserEditRequest request = new UserEditRequest(1L, "testuser", "password123", "password123", "password123", "Test User", "invalid-email");
        ValidationException exception = assertThrows(ValidationException.class, request::validate);
        String expectedMessage = "잘못된 email 형식입니다.\n";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
