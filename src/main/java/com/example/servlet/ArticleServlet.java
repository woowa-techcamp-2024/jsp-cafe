package com.example.servlet;

import java.io.IOException;

import com.example.annotation.Login;
import com.example.dto.SaveArticleRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.Article;
import com.example.service.ArticleService;

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

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		articleService = (ArticleService)config.getServletContext().getAttribute("articleService");
	}

	@Login
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("id");
		SaveArticleRequest dto = DtoCreationUtil.createDto(SaveArticleRequest.class, req);
		dto.validate();
		articleService.savePost(userId, dto);
		resp.sendRedirect("/");
	}

	@Login
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long articleId = Long.parseLong(req.getPathInfo().substring(1));
		Article article = articleService.getArticle(articleId);
		req.setAttribute("article", article);
		req.getRequestDispatcher("/WEB-INF/qna/show.jsp").forward(req, resp);
	}
}
