package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import com.example.dto.SaveArticleRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.Article;
import com.example.service.ArticleService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@DisplayName("ArticleServlet 테스트")
class ArticleServletTest {

	private ArticleServlet articleServlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private RequestDispatcher requestDispatcher;
	private ArticleService articleService;
	private static MockedStatic<DtoCreationUtil> mockedDtoCreationUtil;

	@BeforeEach
	void setUp() throws ServletException {
		articleServlet = new ArticleServlet();
		articleService = mock(ArticleService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		requestDispatcher = mock(RequestDispatcher.class);

		mockedDtoCreationUtil = mockStatic(DtoCreationUtil.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("articleService")).thenReturn(articleService);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
		when(request.getSession()).thenReturn(session);

		articleServlet.init(config);
	}

	@AfterEach
	void tearDown() {
		mockedDtoCreationUtil.close();
	}

	@Test
	@DisplayName("유효한 게시글 작성 요청을 처리할 수 있다")
	void doPost_validRequest_createsArticle() throws IOException {
		// given
		when(session.getAttribute("login")).thenReturn(true);
		when(session.getAttribute("id")).thenReturn("id");

		SaveArticleRequest saveArticleRequest = new SaveArticleRequest("title", "contents");
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(SaveArticleRequest.class), any(HttpServletRequest.class)))
			.thenReturn(saveArticleRequest);

		// when
		articleServlet.doPost(request, response);

		// then
		ArgumentCaptor<SaveArticleRequest> captor = ArgumentCaptor.forClass(SaveArticleRequest.class);
		verify(articleService, times(1)).savePost(eq("id"), captor.capture());
		SaveArticleRequest capturedRequest = captor.getValue();
		assertThat(capturedRequest.title()).isEqualTo("title");
		assertThat(capturedRequest.contents()).isEqualTo("contents");

		verify(response).sendRedirect("/");
	}

	@Test
	@DisplayName("필수 필드가 누락된 경우 예외를 던진다")
	void doPost_missingFields_sendsError() throws IOException {
		// given
		when(session.getAttribute("login")).thenReturn(true);
		when(session.getAttribute("id")).thenReturn("id");

		// No title and contents set up in request
		SaveArticleRequest saveArticleRequest = new SaveArticleRequest(null, null);
		mockedDtoCreationUtil.when(() -> DtoCreationUtil.createDto(eq(SaveArticleRequest.class), any(HttpServletRequest.class)))
			.thenReturn(saveArticleRequest);

		// when
		assertThatThrownBy(() -> articleServlet.doPost(request, response))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("유효한 게시글 조회 요청을 처리할 수 있다")
	void doGet_validArticleId_displaysArticle() throws Exception {
		// given
		Long articleId = 1L;
		Article article = new Article(articleId, "user1", "title", "contents");
		when(request.getPathInfo()).thenReturn("/1");
		when(articleService.getArticle(articleId)).thenReturn(article);

		// when
		articleServlet.doGet(request, response);

		// then
		verify(request).setAttribute("article", article);
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("존재하지 않는 게시글 조회 요청 시 예외를 던진다")
	void doGet_invalidArticleId_sendsError() throws Exception {
		// given
		when(request.getPathInfo()).thenReturn("/1");
		when(articleService.getArticle(1L)).thenThrow(new RuntimeException("article not found"));

		// when
		assertThatThrownBy(() -> articleServlet.doGet(request, response))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("경로 정보가 없는 경우 게시글 작성 폼으로 포워딩한다")
	void doGet_noPathInfo_forwardsToForm() throws Exception {
		// when
		when(request.getPathInfo()).thenReturn(null);
		articleServlet.doGet(request, response);

		// then
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	@DisplayName("로그인 실패 시, 예외 처리된다.")
	void doPost_notLogin() throws Exception {
		// given
		when(session.getAttribute("login")).thenReturn(null);

		// when
		articleServlet.doPost(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
