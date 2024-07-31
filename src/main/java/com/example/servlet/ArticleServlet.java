package com.example.servlet;

import java.io.IOException;

import com.example.annotation.Login;
import com.example.dto.SaveArticleRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.Article;
import com.example.service.ArticleService;
import com.example.service.ReplyService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ArticleServlet", urlPatterns = "/articles/*")
public class ArticleServlet extends HttpServlet {

	private ArticleService articleService;
	private ReplyService replyService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		articleService = (ArticleService)config.getServletContext().getAttribute("articleService");
		replyService = (ReplyService)config.getServletContext().getAttribute("replyService");
	}

	@Login
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("id");
		String userName = (String)session.getAttribute("name");
		SaveArticleRequest dto = DtoCreationUtil.createDto(SaveArticleRequest.class, req);
		dto.validate();
		articleService.savePost(userId, userName, dto);
		resp.sendRedirect("/");
	}

	@Login
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo() == null) {
			req.getRequestDispatcher("/WEB-INF/qna/form.jsp").forward(req, resp);
			return;
		}
		Long articleId = Long.parseLong(req.getPathInfo().substring(1));
		Article article = articleService.getArticle(articleId);
		req.setAttribute("article", article);
		req.setAttribute("replyCount", replyService.getReplyCount(articleId));
		req.setAttribute("replies", replyService.findAll(articleId));
		req.getRequestDispatcher("/WEB-INF/qna/show.jsp").forward(req, resp);
	}

	@Login
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long articleId = Long.parseLong(req.getPathInfo().substring(1));
		String userId = (String)req.getSession().getAttribute("id");
		articleService.deleteArticle(userId, articleId);
		resp.sendRedirect("/");
	}
}
