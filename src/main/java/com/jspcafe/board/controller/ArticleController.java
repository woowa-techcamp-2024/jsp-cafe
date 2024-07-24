package com.jspcafe.board.controller;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.service.ArticleService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/", "/articles/*"})
public class ArticleController extends HttpServlet {
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) {
        ServletContext ctx = config.getServletContext();
        articleService = (ArticleService) ctx.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty() || path.isBlank()) {
            articleList(req, resp);
            return;
        }
        switch (path) {
            case "/form" -> forward("article_form", req, resp);
            default -> articleDetail(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeArticle(req, resp);
    }

    private void forward(String fileName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/board/" + fileName + ".jsp").forward(req, resp);
    }

    private void articleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Article> articles = articleService.findAll();
        req.setAttribute("articles", articles);
        forward("board", req, resp);
    }

    private void writeArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String nickname = req.getParameter("nickname");
        String content = req.getParameter("content");
        articleService.write(title, nickname, content);
        resp.sendRedirect("/");
    }

    private void articleDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().replace("/", "");
        Article article = articleService.findById(id);
        req.setAttribute("article", article);
        forward("article_detail", req, resp);
    }
}
