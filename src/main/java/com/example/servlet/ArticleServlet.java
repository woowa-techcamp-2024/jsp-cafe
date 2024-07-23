package com.example.servlet;

import java.io.IOException;
import java.util.Optional;

import com.example.db.ArticleDatabase;
import com.example.entity.Article;
import com.example.entity.User;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ArticleServlet", urlPatterns = "/articles/*")
public class ArticleServlet extends HttpServlet {

	private ArticleDatabase articleDatabase;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		articleDatabase = (ArticleDatabase)getServletContext().getAttribute("articleDatabase");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String writer = req.getParameter("writer");
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");

		if (writer == null || title == null || contents == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Article article = new Article(null, writer, title, contents);
		articleDatabase.insert(article);
		resp.sendRedirect("/");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo() == null) {
			req.getRequestDispatcher("/qna/form.jsp").forward(req, resp);
			return;
		}
		Long articleId = Long.parseLong(req.getPathInfo().substring(1));
		Optional<Article> articleOptional = articleDatabase.findById(articleId);
		if (articleOptional.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Article article = articleOptional.get();
		req.setAttribute("article", article);

		req.getRequestDispatcher("/qna/show.jsp").forward(req, resp);
	}
}
