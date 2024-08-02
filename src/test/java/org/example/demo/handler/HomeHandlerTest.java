package org.example.demo.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HomeHandlerTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private HomeHandler homeHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        homeHandler = new HomeHandler(postRepository);
    }

    @Test
    void 게시글_목록_요청시_모든_게시글_표시() throws ServletException, IOException {
        // Given
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, new User(1L, "user1", "password1", "User 1", "user1@example.com"), "제목1", "내용1", LocalDateTime.now(), new ArrayList<>()));
        posts.add(new Post(2L, new User(2L, "user2", "password2", "User 2", "user2@example.com"), "제목2", "내용2", LocalDateTime.now(), new ArrayList<>()));

        when(postRepository.getPosts()).thenReturn(posts);
        when(request.getRequestDispatcher("/WEB-INF/index.jsp")).thenReturn(requestDispatcher);

        // When
        homeHandler.handleGetPosts(request, response, new ArrayList<>());

        // Then
        verify(request).setAttribute("posts", posts);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 게시글이_없을때_빈_목록_표시() throws ServletException, IOException {
        // Given
        List<Post> emptyPosts = new ArrayList<>();

        when(postRepository.getPosts()).thenReturn(emptyPosts);
        when(request.getRequestDispatcher("/WEB-INF/index.jsp")).thenReturn(requestDispatcher);

        // When
        homeHandler.handleGetPosts(request, response, new ArrayList<>());

        // Then
        verify(request).setAttribute("posts", emptyPosts);
        verify(requestDispatcher).forward(request, response);
    }
}