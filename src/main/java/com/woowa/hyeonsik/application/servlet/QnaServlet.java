package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.Page;
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

public class QnaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaServlet.class);
    private final ArticleService articleService;

    public QnaServlet(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageParam = request.getParameter("page");
        long page = 1;
        if (!(pageParam == null || pageParam.isEmpty())) {
            page = Long.parseLong(pageParam);
        }
        logger.debug("질문 목록을 조회합니다. page: {}", page);

        Page<Article> list = articleService.list(page);
        logger.debug("페이지: {}, 끝페이지: {}, 글목록 {}", list.getNumberOfPage(), list.getNumberOfEnd(), list.getContent());
        request.setAttribute("questions", list);

        SendPageUtil.forward("/template/index.jsp", getServletContext(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> parse = JsonParser.parse(request.getReader());
        String title = (String) parse.get("title");
        String contents = (String) parse.get("contents");

        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 세션에서 글쓴이 정보를 가져온다.
        User user = (User) request.getSession(false).getAttribute("user");
        String writer = user.getUserId();
        logger.debug("글쓰기 요청 수행! writer: {}, title: {}, contents: {}", writer, title, contents);

        // 게시글 작성
        Article article = new Article(null, writer, title, contents);
        articleService.write(article);
    }
}
