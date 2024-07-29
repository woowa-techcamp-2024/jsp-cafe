package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QnaPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaPathServlet.class);
    private final ArticleService articleService;

    public QnaPathServlet(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        long questionId = Long.parseLong(split[split.length - 1]);
        logger.debug("게시글을 조회합니다. qnaID: {}", questionId);

        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 게시글 조회 및 반환
        Article foundArticle = articleService.findById(questionId);
        request.setAttribute("article", foundArticle);

        SendPageUtil.forward("/template/qna/show.jsp", getServletContext(), request, response);
    }
}
