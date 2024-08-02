package org.example.demo.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.domain.Comment;
import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.exception.UnauthorizedException;
import org.example.demo.model.PostCreateDao;
import org.example.demo.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostHandlerTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;

    private PostHandler postHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postHandler = new PostHandler(postRepository);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void 로그인한_사용자가_존재하는_게시글_조회시_게시글_표시() throws ServletException, IOException {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        List<String> pathVariables = List.of(postId.toString());
        Post post = new Post(postId, new User(userId, "user1", null, "User 1", null), "제목", "내용", LocalDateTime.now(), new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(userId);
        when(postRepository.getPost(postId)).thenReturn(Optional.of(post));
        when(request.getRequestDispatcher("/WEB-INF/post/show.jsp")).thenReturn(requestDispatcher);

        // When
        postHandler.handleGetPost(request, response, pathVariables);

        // Then
        verify(request).setAttribute("post", post);
        verify(request).setAttribute("comments", post.getComments());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 로그인하지_않은_사용자가_게시글_조회시_로그인_페이지로_이동() throws ServletException, IOException {
        // Given
        List<String> pathVariables = List.of("1");
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/user/login.jsp")).thenReturn(requestDispatcher);

        // When
        postHandler.handleGetPost(request, response, pathVariables);

        // Then
        verify(request).setAttribute("error", "User not logged in");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 로그인한_사용자가_유효한_데이터로_게시글_생성시_성공적으로_생성() throws ServletException, IOException {
        // Given
        Long userId = 1L;
        String title = "새 게시글";
        String contents = "게시글 내용";
        List<String> pathVariables = new ArrayList<>();

        when(session.getAttribute("user")).thenReturn(userId);
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("contents")).thenReturn(contents);

        // When
        postHandler.handleCreatePost(request, response, pathVariables);

        // Then
        verify(postRepository).addPost(any(PostCreateDao.class));
        verify(response).sendRedirect("/");
    }

    @Test
    void 권한이_있는_사용자가_게시글_수정시_성공적으로_수정() throws ServletException, IOException {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        String newTitle = "수정된 제목";
        String newContents = "수정된 내용";
        List<String> pathVariables = List.of(postId.toString());
        Post post = new Post(postId, new User(userId, "user1", null, "User 1", null), "원본 제목", "원본 내용", LocalDateTime.now(), new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(userId);
        when(postRepository.getPost(postId)).thenReturn(Optional.of(post));
        when(request.getParameter("title")).thenReturn(newTitle);
        when(request.getParameter("contents")).thenReturn(newContents);

        // When
        postHandler.handleUpdatePost(request, response, pathVariables);

        // Then
        verify(postRepository).updatePost(postId, newTitle, newContents);
        verify(response).sendRedirect("/posts/" + postId);
    }

    @Test
    void 권한이_없는_사용자가_게시글_수정시_UnauthorizedException_발생() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        Long differentUserId = 2L;
        List<String> pathVariables = List.of(postId.toString());
        Post post = new Post(postId, new User(differentUserId, "user2", null, "User 2", null), "제목", "내용", LocalDateTime.now(), new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(userId);
        when(postRepository.getPost(postId)).thenReturn(Optional.of(post));

        // When & Then
        assertThrows(UnauthorizedException.class, () -> postHandler.handleUpdatePost(request, response, pathVariables));
    }

    @Test
    void 권한이_있는_사용자가_댓글이_없는_게시글_삭제시_성공적으로_삭제() throws ServletException, IOException {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        List<String> pathVariables = List.of(postId.toString());
        Post post = new Post(postId, new User(userId, "user1", null, "User 1", null), "제목", "내용", LocalDateTime.now(), new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(userId);
        when(postRepository.getPost(postId)).thenReturn(Optional.of(post));

        // When
        postHandler.handleDeletePost(request, response, pathVariables);

        // Then
        verify(postRepository).deletePost(postId);
        verify(response).sendRedirect("/");
    }

    @Test
    void 다른_사용자의_댓글이_있는_게시글_삭제시_IllegalArgumentException_발생() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        Long otherUserId = 2L;
        List<String> pathVariables = List.of(postId.toString());
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, postId, new User(otherUserId, "user2", null, "User 2", null), "댓글 내용", LocalDateTime.now()));
        Post post = new Post(postId, new User(userId, "user1", null, "User 1", null), "제목", "내용", LocalDateTime.now(), comments);

        when(session.getAttribute("user")).thenReturn(userId);
        when(postRepository.getPost(postId)).thenReturn(Optional.of(post));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> postHandler.handleDeletePost(request, response, pathVariables));
    }

    @Test
    void 존재하지_않는_게시글_조회시_NotFoundExceptoin_발생() {
        // Given
        Long nonExistentPostId = 999L;
        List<String> pathVariables = List.of(nonExistentPostId.toString());
        when(session.getAttribute("user")).thenReturn(1L);
        when(postRepository.getPost(nonExistentPostId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundExceptoin.class, () -> postHandler.handleGetPost(request, response, pathVariables));
    }
}