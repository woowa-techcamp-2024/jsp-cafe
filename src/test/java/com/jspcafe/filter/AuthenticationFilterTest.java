package com.jspcafe.filter;

import com.jspcafe.test_util.StubFilterChain;
import com.jspcafe.test_util.StubHttpServletRequest;
import com.jspcafe.test_util.StubHttpServletResponse;
import com.jspcafe.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationFilterTest {
    private AuthenticationFilter authenticationFilter;
    private StubHttpServletRequest request;
    private StubHttpServletResponse response;
    private StubFilterChain filterChain;

    @BeforeEach
    void setUp() {
        authenticationFilter = new AuthenticationFilter();
        request = new StubHttpServletRequest();
        response = new StubHttpServletResponse();
        filterChain = new StubFilterChain();
    }

    @Test
    void 로그인한_사용자는_게시글관련_페이지로_접속하면_다음필터에_넘겨준다() throws ServletException, IOException {
        // Given
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", User.create("test@test", "testName", "testPassword"));

        // When
        authenticationFilter.doFilter(request, response, filterChain);

        // Then
        assertTrue(filterChain.isDoFilterCalled());
    }

    @Test
    void 로그인하지_않은_사용자는_게시글관련_페이지에_접속하면_로그인페이지로_리다이렉트_된다() throws ServletException, IOException {
        // Given
        HttpSession session = request.getSession();
        session.invalidate();

        // When
        authenticationFilter.doFilter(request, response, filterChain);

        // Then
        assertEquals("/users/login", response.getRedirectLocation());
    }
}
