package com.example.servlet;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.example.dto.UpdateArticleRequest;
import com.example.entity.Article;
import com.example.service.ArticleService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

class ArticleUpdateServletTest {

	private ArticleUpdateServlet servlet;
	private ArticleService articleService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher requestDispatcher;

	@BeforeEach
	void setUp() throws Exception {
		servlet = new ArticleUpdateServlet();
		articleService = mock(ArticleService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		requestDispatcher = mock(RequestDispatcher.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("articleService")).thenReturn(articleService);
		when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

		servlet.init(config);
	}

	@Test
	void doGet() throws Exception {
		// given
		long articleId = 1L;
		Article article = new Article(articleId, "1", "title", "content", LocalDateTime.now(), false, "username");
		when(request.getPathInfo()).thenReturn("/" + articleId);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		when(articleService.getArticle(articleId)).thenReturn(article);
		willDoNothing().given(articleService).checkValidation(anyString(), anyLong());

		// when
		servlet.doGet(request, response);

		// then
		verify(request).setAttribute("article", article);
		verify(requestDispatcher).forward(request, response);
	}

	@Test
	void doPut() throws Exception {
		// given
		String userId = "userId";
		String userName = "userName";
		UpdateArticleRequest dto = new UpdateArticleRequest("title", "content");
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		when(request.getSession().getAttribute("id")).thenReturn(userId);
		when(request.getSession().getAttribute("name")).thenReturn(userName);
		when(request.getPathInfo()).thenReturn("/1");

		// when
		servlet.doPut(request, response);

		// then
		ArgumentCaptor<Long> articleIdCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<UpdateArticleRequest> dtoCaptor = ArgumentCaptor.forClass(UpdateArticleRequest.class);
		verify(articleService).updateArticle(eq(userId), eq(userName), articleIdCaptor.capture(), dtoCaptor.capture());
		assertThat(articleIdCaptor.getValue()).isEqualTo(1L);
		assertThat(dtoCaptor.getValue().title()).isNull();
		assertThat(dtoCaptor.getValue().contents()).isNull();
		verify(response).sendRedirect("/index.jsp");
	}
}
