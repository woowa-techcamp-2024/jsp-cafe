package org.example.demo.validator;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.domain.Comment;
import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthValidatorTest {
    AuthValidator authValidator;
    RequestDispatcher requestDispatcher;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;

    @BeforeEach
    void setUp() {
        authValidator = new AuthValidator();
        requestDispatcher = mock(RequestDispatcher.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    @DisplayName("로그인된 유저는 checkLoggedIn 메서드를 통과한다.")
    void checkLoggedIn() throws ServletException, IOException {
        // Given
        User user = new User(1L, "user1", "password", "User One", "user1@example.com");
        when(request.getSession()).thenReturn(session); // mock session
        when(session.getAttribute("user")).thenReturn(user);

        // When, Then
        authValidator.checkLoggedIn(request, response);
    }

//    @Test
//    @DisplayName("로그인 안된 유저는 checkLoggedIn 메서드를 통과한다.")
//    void checkNotLoggedIn() throws ServletException, IOException {
//        // Given
//        when(request.getSession()).thenReturn(null);
//
//        // When, Then
//        authValidator.checkLoggedIn(request, response);
//    }

    @Test
    void testCheckIdenticalUser_Post_Authorized() {
        // Given
        User user = new User(1L, "user1", "password", "User One", "user1@example.com");
        Post post = new Post(1L, user, "Post Title", "Post Contents", null, null);

        // When, Then
        authValidator.checkIdenticalUser(request, 1L, post);
    }

    @Test
    void testCheckIdenticalUser_Post_NotAuthorized() {
        // Given
        User user = new User(1L, "user1", "password", "User One", "user1@example.com");
        Post post = new Post(1L, user, "Post Title", "Post Contents", null, null);

        // When
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () ->
                authValidator.checkIdenticalUser(request, 2L, post)
        );

        // Then
        assertEquals("User not authorized", thrown.getMessage());
    }

    @Test
    void testCheckIdenticalUser_Comment_Authorized() {
        // Given
        User user = new User(1L, "user1", "password", "User One", "user1@example.com");
        Comment comment = new Comment(1L, 1L, user, "Comment Contents", null);

        // When, Then
        authValidator.checkIdenticalUser(request, 1L, comment);
    }

    @Test
    void testCheckIdenticalUser_Comment_NotAuthorized() {
        // Given
        User user = new User(1L, "user1", "password", "User One", "user1@example.com");
        Comment comment = new Comment(1L, 1L, user, "Comment Contents", null);

        // When
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class, () ->
                authValidator.checkIdenticalUser(request, 2L, comment)
        );

        // Then
        assertEquals("User not authorized", thrown.getMessage());
    }
}
