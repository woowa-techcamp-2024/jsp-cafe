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

@WebServlet("/questions/*")
public class QnaPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaPathServlet.class);
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleService = (ArticleService) getServletContext().getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        long questionId = Long.parseLong(split[split.length - 1]);
        logger.debug("게시글을 조회합니다. qnaID: {}", questionId);

        Article foundArticle = articleService.findById(questionId);
        request.setAttribute("article", foundArticle);

        SendPageUtil.forward("/template/qna/show.jsp", getServletContext(), request, response);
    }
}
