package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.domain.Page;
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
        String pageString = req.getParameter("page");
        String sizeString = req.getParameter("size");
        int page =  pageString == null || pageString.isBlank()  ? 1 : Integer.parseInt(req.getParameter("page"));
        int size =  sizeString == null || sizeString.isBlank() ? 15 : Integer.parseInt(req.getParameter("size"));
        long totalSize = articleDao.count();
        List<Article> contents = articleDao.findAll(size,page);
        req.setAttribute("articles",new Page<>(size,page,totalSize,contents));

        req.getRequestDispatcher("/qnaList.jsp").forward(req,resp);
    }
}
