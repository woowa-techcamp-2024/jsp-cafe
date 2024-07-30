package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class QnaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaServlet.class);
    private final ArticleService articleService;

    public QnaServlet(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> list = articleService.list();
        request.setAttribute("questions", list);
        logger.debug("전체 질문 목록을 조회합니다. size: {}", list.size());

        SendPageUtil.forward("/template/index.jsp", getServletContext(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        logger.debug("글쓰기 요청도착! writer: {}, title: {}, contents: {}", writer, title, contents);

        Article article = new Article(null, writer, title, contents);
        articleService.write(article);

        SendPageUtil.redirect("/", getServletContext(), response);
    }
}
