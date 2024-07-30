package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleControlServlet extends HttpServlet {
    private ArticleDao articleDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || pathInfo.isEmpty() || pathInfo.split("/").length != 2) {
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        try {
            Long articleId = Long.parseLong(pathInfo.substring(1));

            Article article = articleDao.findById(articleId)
                    .orElseThrow(() -> new HttpNotFoundException(articleId + "번의 게시글을 찾을 수 없습니다."));
            req.setAttribute("article",article);
            req.getRequestDispatcher("/templates/qna/show.jsp").forward(req, resp);
        }catch(NumberFormatException e ){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
    }
}
