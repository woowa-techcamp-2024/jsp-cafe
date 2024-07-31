package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

import com.example.annotation.Login;
import com.example.entity.Reply;
import com.example.service.ReplyService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ReplyServlet", urlPatterns = "/replies/*")
public class ReplyServlet extends HttpServlet {

	private ReplyService replyService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		replyService = (ReplyService)config.getServletContext().getAttribute("replyService");
	}

	@Login
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StringBuilder jsonBuffer = new StringBuilder();
		String line;
		try (BufferedReader reader = req.getReader()) {
			while ((line = reader.readLine()) != null) {
				jsonBuffer.append(line);
			}
		}

		String jsonString = jsonBuffer.toString();
		long articleId = 0;
		String content = null;

		jsonString = jsonString.replace("{", "").replace("}", "").replace("\"", "");
		String[] keyValuePairs = jsonString.split(",");
		for (String pair : keyValuePairs) {
			String[] entry = pair.split(":");
			if (entry[0].trim().equals("articleId")) {
				articleId = Long.parseLong(entry[1].trim());
			} else if (entry[0].trim().equals("content")) {
				content = entry[1].trim();
			}
		}

		String userName = (String)req.getSession().getAttribute("name");
		String userId = (String)req.getSession().getAttribute("id");
		Reply reply = new Reply(null, content, LocalDateTime.now(), false, articleId, userId, userName);
		Long replyId = replyService.addReply(reply);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		String jsonResponse = String.format("{\"id\": %d, \"author\": \"%s\", \"time\": \"%s\", \"content\": \"%s\"}",
			replyId, reply.getUserName(), reply.getCreatedAt().toString(), reply.getContents());
		resp.getWriter().write(jsonResponse);
	}

	@Login
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long replyId = Long.parseLong(req.getPathInfo().substring(1));
		replyService.deleteReply(replyId, (String)req.getSession().getAttribute("id"));
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}