package com.codesquad.cafe.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.model.dto.PostUpdateRequest;
import org.junit.jupiter.api.Test;

class PostUpdateRequestTest {

    @Test
    void testValidPostUpdateRequest() {
        PostUpdateRequest request = new PostUpdateRequest(1L, "Valid Title", "Valid Content", "filename.jpg");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void testInvalidPostUpdateRequest_MissingFields() {
        PostUpdateRequest request = new PostUpdateRequest(null, "", "", "");
        ValidationException exception = assertThrows(ValidationException.class, request::validate);
        String expectedMessage = "authorId는 1이상의 자연수여야 합니다.\ntitle은 필수입니다.\ncontent는 필수입니다.\n";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
