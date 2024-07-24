package com.woowa.hyeonsik.servlet;

import com.woowa.hyeonsik.domain.Article;
import com.woowa.hyeonsik.service.ArticleService;
import com.woowa.hyeonsik.util.SendPageUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/questions")
public class QnaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaServlet.class);
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleService = (ArticleService) getServletContext().getAttribute("articleService");  // <- FIXME 컴파일 타임에 오타 에러를 발견할 수 없음
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(405);
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
