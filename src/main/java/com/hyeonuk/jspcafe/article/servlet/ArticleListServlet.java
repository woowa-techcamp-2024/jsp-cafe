package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ArticleListServlet extends HttpServlet {
    private ArticleDao articleDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        List<Article> articles = articleDao.findAll();
        req.setAttribute("articles",articles);

        req.getRequestDispatcher("/qnaList.jsp").forward(req,resp);
    }
}
