package org.example.demo.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.domain.User;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;
import org.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;

    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userHandler = new UserHandler(userRepository);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void 사용자_목록_조회시_모든_사용자_표시() throws ServletException, IOException {
        // Given
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "user1", "password1", "User 1", "user1@example.com"));
        users.add(new User(2L, "user2", "password2", "User 2", "user2@example.com"));
        when(userRepository.getUsers()).thenReturn(users);
        when(request.getRequestDispatcher("/WEB-INF/user/list.jsp")).thenReturn(requestDispatcher);

        // When
        userHandler.handleUserList(request, response, new ArrayList<>());

        // Then
        verify(request).setAttribute("users", users);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 존재하는_사용자_프로필_조회시_사용자_정보_표시() throws ServletException, IOException {
        // Given
        Long userId = 1L;
        User user = new User(userId, "user1", "password1", "User 1", "user1@example.com");
        List<String> pathVariables = List.of(userId.toString());
        when(userRepository.getUser(userId)).thenReturn(Optional.of(user));
        when(request.getRequestDispatcher("/WEB-INF/user/profile.jsp")).thenReturn(requestDispatcher);

        // When
        userHandler.handleUserProfile(request, response, pathVariables);

        // Then
        verify(request).setAttribute("user", user);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 존재하지_않는_사용자_프로필_조회시_NotFoundExceptoin_발생() {
        // Given
        Long nonExistentUserId = 999L;
        List<String> pathVariables = List.of(nonExistentUserId.toString());
        when(userRepository.getUser(nonExistentUserId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundExceptoin.class, () -> userHandler.handleUserProfile(request, response, pathVariables));
    }

    @Test
    void 유효한_데이터로_사용자_생성시_성공적으로_생성() throws IOException {
        // Given
        String userId = "newuser";
        String password = "password";
        String name = "New User";
        String email = "newuser@example.com";
        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(userRepository.getUserByUserId(userId)).thenReturn(Optional.empty());

        // When
        userHandler.handleUserCreate(request, response, new ArrayList<>());

        // Then
        verify(userRepository).addUser(any(UserCreateDao.class));
        verify(response).sendRedirect("/users");
    }

    @Test
    void 이미_존재하는_사용자_아이디로_생성_시도시_IllegalArgumentException_발생() {
        // Given
        String existingUserId = "existinguser";
        when(request.getParameter("userId")).thenReturn(existingUserId);
        when(userRepository.getUserByUserId(existingUserId)).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userHandler.handleUserCreate(request, response, new ArrayList<>()));
    }

    @Test
    void 올바른_비밀번호로_사용자_정보_업데이트시_성공적으로_업데이트() throws IOException {
        // Given
        Long userId = 1L;
        String password = "password";
        String newName = "Updated Name";
        String newEmail = "updated@example.com";
        List<String> pathVariables = List.of(userId.toString());
        User existingUser = new User(userId, "user1", password, "Original Name", "original@example.com");

        when(userRepository.getUser(userId)).thenReturn(Optional.of(existingUser));
        when(request.getParameter("passwordCheck")).thenReturn(password);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("name")).thenReturn(newName);
        when(request.getParameter("email")).thenReturn(newEmail);

        // When
        userHandler.handleUserUpdate(request, response, pathVariables);

        // Then
        verify(userRepository).updateUser(any(UserUpdateDao.class));
        verify(response).sendRedirect("/users/" + userId);
    }

    @Test
    void 잘못된_비밀번호로_사용자_정보_업데이트시_IllegalArgumentException_발생() {
        // Given
        Long userId = 1L;
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        List<String> pathVariables = List.of(userId.toString());
        User existingUser = new User(userId, "user1", correctPassword, "Original Name", "original@example.com");

        when(userRepository.getUser(userId)).thenReturn(Optional.of(existingUser));
        when(request.getParameter("passwordCheck")).thenReturn(wrongPassword);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userHandler.handleUserUpdate(request, response, pathVariables));
    }

    @Test
    void 올바른_아이디와_비밀번호로_로그인시_성공적으로_로그인() throws IOException, ServletException {
        // Given
        String userId = "user1";
        String password = "password1";
        User user = new User(1L, userId, password, "User 1", "user1@example.com");

        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("password")).thenReturn(password);
        when(userRepository.getUserByUserId(userId)).thenReturn(Optional.of(user));

        // When
        userHandler.handleUserLogin(request, response, new ArrayList<>());

        // Then
        verify(session).setAttribute("user", user.getId());
        verify(response).sendRedirect("/users/" + user.getId());
    }

    @Test
    void 잘못된_아이디나_비밀번호로_로그인시_로그인_실패() throws IOException, ServletException {
        // Given
        String userId = "wronguser";
        String password = "wrongpassword";

        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("password")).thenReturn(password);
        when(userRepository.getUserByUserId(userId)).thenReturn(Optional.empty());
        when(request.getRequestDispatcher("/WEB-INF/user/login_failed.jsp")).thenReturn(requestDispatcher);

        // When
        userHandler.handleUserLogin(request, response, new ArrayList<>());

        // Then
        verify(request).setAttribute("error", "아이디 또는 패스워드가 틀렸습니다.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 로그아웃_요청시_세션_무효화_및_홈으로_리다이렉트() throws IOException {
        // When
        userHandler.handleUserLogout(request, response, new ArrayList<>());

        // Then
        verify(session).invalidate();
        verify(response).sendRedirect("/");
    }
}