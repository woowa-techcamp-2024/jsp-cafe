package org.example.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class SessionUtilTest {

    @Test
    public void testExtractUserId_sessionNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(null);

        Optional<String> result = SessionUtil.extractUserId(request);

        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractUserId_userIdNotPresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(null);

        Optional<String> result = SessionUtil.extractUserId(request);

        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void testExtractUserId_userIdPresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUserId");

        Optional<String> result = SessionUtil.extractUserId(request);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("testUserId", result.get());
    }
}
