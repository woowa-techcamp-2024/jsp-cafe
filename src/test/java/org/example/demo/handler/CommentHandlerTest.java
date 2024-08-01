package org.example.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.domain.Comment;
import org.example.demo.domain.User;
import org.example.demo.exception.UnauthorizedException;
import org.example.demo.model.CommentCreateDao;
import org.example.demo.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentHandlerTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private CommentHandler commentHandler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentHandler = new CommentHandler(commentRepository);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void 유효한_댓글_데이터로_댓글_생성시_생성된_댓글과_업데이트된_댓글_목록_반환() throws Exception {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        String contents = "테스트 댓글";
        List<String> pathVariables = List.of(postId.toString());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        when(request.getParameter("contents")).thenReturn(contents);

        Comment createdComment = new Comment(1L, postId, new User(userId, "user1", null, "사용자 1", null), contents, LocalDateTime.now());
        when(commentRepository.saveComment(any(CommentCreateDao.class))).thenReturn(createdComment);

        // 추가된 댓글 목록 생성
        List<Comment> additionalComments = new ArrayList<>();
        additionalComments.add(new Comment(2L, postId, new User(2L, "user2", null, "사용자 2", null), "추가 댓글 1", LocalDateTime.now()));
        additionalComments.add(new Comment(3L, postId, new User(3L, "user3", null, "사용자 3", null), "추가 댓글 2", LocalDateTime.now()));
        when(commentRepository.getMoreComments(eq(postId), eq(createdComment.getId()))).thenReturn(additionalComments);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // When
        commentHandler.createComment(request, response, pathVariables);

        // Then
        verify(commentRepository).saveComment(any(CommentCreateDao.class));
        verify(commentRepository).getMoreComments(eq(postId), eq(createdComment.getId()));
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String responseContent = stringWriter.toString();
        List<Comment> returnedComments = objectMapper.readValue(responseContent, objectMapper.getTypeFactory().constructCollectionType(List.class, Comment.class));

        assertFalse(returnedComments.isEmpty());
        assertEquals(3, returnedComments.size());  // 생성된 댓글 1개 + 추가 댓글 2개
        assertEquals(createdComment, returnedComments.get(0));
        assertEquals(additionalComments.get(0), returnedComments.get(1));
        assertEquals(additionalComments.get(1), returnedComments.get(2));
    }

    @Test
    void 유효한_댓글_ID로_댓글_삭제시_성공적으로_삭제() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;
        List<String> pathVariables = List.of("1", commentId.toString());

        Comment existingComment = new Comment(commentId, 1L, new User(userId, "user1", null, "사용자 1", null), "테스트 댓글", LocalDateTime.now());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        when(commentRepository.getComment(commentId)).thenReturn(Optional.of(existingComment));

        // When
        commentHandler.deleteComment(request, response, pathVariables);

        // Then
        verify(commentRepository).deleteComment(commentId);
        verify(response).setContentType("application/json");
    }

    @Test
    void 권한이_없는_사용자가_댓글_삭제시_UnauthorizedException_발생() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;
        Long differentUserId = 2L;
        List<String> pathVariables = List.of("1", commentId.toString());

        Comment existingComment = new Comment(commentId, 1L, new User(differentUserId, "user2", null, "사용자 2", null), "테스트 댓글", LocalDateTime.now());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        when(commentRepository.getComment(commentId)).thenReturn(Optional.of(existingComment));

        // When & Then
        assertThrows(UnauthorizedException.class, () -> commentHandler.deleteComment(request, response, pathVariables));
        verify(commentRepository, never()).deleteComment(commentId);
    }

    @Test
    void 유효한_댓글_데이터와_권한_있는_사용자가_댓글_업데이트시_성공적으로_업데이트() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;
        String newContents = "업데이트된 댓글";
        List<String> pathVariables = List.of("1", commentId.toString());

        Comment existingComment = new Comment(commentId, 1L, new User(userId, "user1", null, "사용자 1", null), "원본 댓글", LocalDateTime.now());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        when(request.getParameter("contents")).thenReturn(newContents);
        when(commentRepository.getComment(commentId)).thenReturn(Optional.of(existingComment));

        // When
        commentHandler.updateComment(request, response, pathVariables);

        // Then
        verify(commentRepository).updateComment(commentId, newContents);
    }

    @Test
    void 권한이_없는_사용자가_댓글_업데이트시_UnauthorizedException_발생() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;
        Long differentUserId = 2L;
        String newContents = "업데이트된 댓글";
        List<String> pathVariables = List.of("1", commentId.toString());

        Comment existingComment = new Comment(commentId, 1L, new User(differentUserId, "user2", null, "사용자 2", null), "원본 댓글", LocalDateTime.now());

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(userId);
        when(request.getParameter("contents")).thenReturn(newContents);
        when(commentRepository.getComment(commentId)).thenReturn(Optional.of(existingComment));

        // When & Then
        assertThrows(UnauthorizedException.class, () -> commentHandler.updateComment(request, response, pathVariables));
        verify(commentRepository, never()).updateComment(anyLong(), anyString());
    }
}