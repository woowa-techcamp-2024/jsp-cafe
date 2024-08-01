package com.example.servlet;

import java.io.IOException;

import com.example.annotation.Login;
import com.example.dto.UpdateArticleRequest;
import com.example.dto.util.DtoCreationUtil;
import com.example.entity.Article;
import com.example.service.ArticleService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ArticleUpdateServlet", urlPatterns = "/articles/edit/*")
public class ArticleUpdateServlet extends HttpServlet {

	private ArticleService articleService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		articleService = (ArticleService)config.getServletContext().getAttribute("articleService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long articleId = Long.parseLong(req.getPathInfo().substring(1));
		Article article = articleService.getArticle(articleId);
		articleService.checkValidation((String)req.getSession().getAttribute("id"), articleId);
		req.setAttribute("article", article);
		req.getRequestDispatcher("/WEB-INF/qna/edit.jsp").forward(req, resp);
	}

	@Login
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = (String)req.getSession().getAttribute("id");
		String userName = (String)req.getSession().getAttribute("name");
		UpdateArticleRequest dto = DtoCreationUtil.createDto(UpdateArticleRequest.class, req);

		articleService.updateArticle(userId, userName, Long.parseLong(req.getPathInfo().substring(1)), dto);
		resp.sendRedirect("/index.jsp");
	}
}
