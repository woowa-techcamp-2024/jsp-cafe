package com.jspcafe.board.controller;

import com.jspcafe.board.service.ArticleService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/", "/articles/*"})
public class ArticleController extends HttpServlet {
    private ArticleService articleService;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        articleService = (ArticleService) ctx.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty() || path.isBlank()) {
            forward("board", req, resp);
        }
        switch (path) {
            case "/form" -> forward("article_form", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeArticle(req, resp);
    }

    private void forward(String fileName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/board/" + fileName + ".jsp").forward(req, resp);
    }

    private void writeArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String nickname = req.getParameter("nickname");
        String content = req.getParameter("content");
        articleService.write(title, nickname, content);
        resp.sendRedirect("/");
    }
}
