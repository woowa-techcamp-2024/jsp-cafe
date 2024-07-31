package com.example.servlet;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.entity.Reply;
import com.example.service.ReplyService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@DisplayName("ReplyServlet 테스트")
class ReplyServletTest {

	private ReplyServlet replyServlet;
	private ReplyService replyService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private PrintWriter writer;

	@BeforeEach
	void setUp() throws ServletException, IOException {
		replyServlet = new ReplyServlet();
		replyService = mock(ReplyService.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		writer = mock(PrintWriter.class);

		ServletConfig config = mock(ServletConfig.class);
		ServletContext context = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(context);
		when(context.getAttribute("replyService")).thenReturn(replyService);
		when(request.getSession()).thenReturn(session);
		when(response.getWriter()).thenReturn(writer);

		replyServlet.init(config);
	}

	@Test
	@DisplayName("POST 요청을 처리하여 댓글을 추가할 수 있다")
	void doPost_addsReply() throws IOException, ServletException {
		String jsonContent = "{\"articleId\": 1, \"content\": \"reply content\"}";
		when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonContent)));
		when(request.getSession().getAttribute("id")).thenReturn("userId");
		when(request.getSession().getAttribute("name")).thenReturn("userName");
		when(replyService.addReply(any(Reply.class))).thenReturn(1L);

		replyServlet.doPost(request, response);

		verify(replyService).addReply(any(Reply.class));
		verify(response).setContentType("application/json");
		verify(response).setCharacterEncoding("UTF-8");
		verify(response.getWriter()).write(anyString());
	}

	@Test
	@DisplayName("DELETE 요청을 처리하여 댓글을 삭제할 수 있다")
	void doDelete_deletesReply() throws IOException, ServletException {
		long replyId = 1L;
		when(request.getPathInfo()).thenReturn("/1");
		when(request.getSession().getAttribute("id")).thenReturn("userId");

		replyServlet.doDelete(request, response);

		verify(replyService).deleteReply(replyId, "userId");
		verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}
