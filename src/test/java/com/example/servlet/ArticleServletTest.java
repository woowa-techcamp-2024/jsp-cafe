package com.example.servlet;

import com.example.db.ArticleMemoryDatabase;
import com.example.entity.Article;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("ArticleServlet 테스트")
class ArticleServletTest {

	private ArticleServlet articleServlet;
	private ArticleMemoryDatabase articleMemoryDatabase;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;

	@BeforeEach
	void setUp() throws ServletException {
		articleServlet = new ArticleServlet();
		articleMemoryDatabase = mock(ArticleMemoryDatabase.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("articleDatabase")).thenReturn(articleMemoryDatabase);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

		articleServlet.init(config);
	}

	@Test
	@DisplayName("유효한 게시글 작성 요청을 처리할 수 있다")
	void doPost_validRequest_createsArticle() throws IOException {
		// given
		when(request.getParameter("writer")).thenReturn("writer");
		when(request.getParameter("title")).thenReturn("title");
		when(request.getParameter("contents")).thenReturn("contents");

		// when
		articleServlet.doPost(request, response);

		// then
		ArgumentCaptor<Article> articleCaptor = ArgumentCaptor.forClass(Article.class);
		verify(articleMemoryDatabase).insert(articleCaptor.capture());
		Article insertedArticle = articleCaptor.getValue();
		assertThat(insertedArticle.getWriter()).isEqualTo("writer");
		assertThat(insertedArticle.getTitle()).isEqualTo("title");
		assertThat(insertedArticle.getContents()).isEqualTo("contents");

		verify(response).sendRedirect("/");
	}

	@Test
	@DisplayName("필수 필드가 누락된 경우 예외를 던진다")
	void doPost_missingFields_sendsError() throws IOException {
		// given
		when(request.getParameter("writer")).thenReturn(null);

		// when
		articleServlet.doPost(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	@DisplayName("유효한 게시글 조회 요청을 처리할 수 있다")
	void doGet_validArticleId_displaysArticle() throws Exception {
		// given
		Article article = new Article(1L, "writer", "title", "contents");
		when(request.getPathInfo()).thenReturn("/1");
		when(articleMemoryDatabase.findById(1L)).thenReturn(Optional.of(article));

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
		when(articleMemoryDatabase.findById(1L)).thenReturn(Optional.empty());

		// when
		articleServlet.doGet(request, response);

		// then
		verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
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
}
