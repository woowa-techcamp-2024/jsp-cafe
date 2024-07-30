package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.util.JsonParser;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class QnaPathServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaPathServlet.class);
    private final ArticleService articleService;

    public QnaPathServlet(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 ID 가져오기
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/questions/".length());
        String[] split = requestURI.split("/");
        long questionId = Long.parseLong(split[0]);
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

        if (requestURI.endsWith("/form")) {
            // 게시글 수정 Form
            SendPageUtil.forward("/template/qna/form.jsp", getServletContext(), request, response);
        } else {
            // 게시글 조회
            SendPageUtil.forward("/template/qna/show.jsp", getServletContext(), request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 ID 가져오기
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/questions/".length());
        String[] split = requestURI.split("/");
        long articleId = Long.parseLong(split[0]);

        // 게시글 정보 JSON 가져오기
        Map<String, Object> parse = JsonParser.parse(request.getReader());
        String title = (String) parse.get("title");
        String contents = (String) parse.get("contents");
        logger.debug("게시글을 수정합니다. qnaID: {}, title: {}, contents: {}", articleId, title, contents);

        // 인증 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 동작
        Article article = new Article(articleId, sessionUser.getUserId(), title, contents);
        articleService.update(article, sessionUser.getUserId());

        // 반환
//        SendPageUtil.redirect("/questions/" + articleId, getServletContext(), response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 ID 가져오기
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/questions/".length());
        String[] split = requestURI.split("/");
        long articleId = Long.parseLong(split[0]);
        logger.debug("게시글 삭제 요청을 수행합니다. 글번호: {}", articleId);

        // 인증 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        articleService.remove(articleId, sessionUser.getUserId());
    }
}
