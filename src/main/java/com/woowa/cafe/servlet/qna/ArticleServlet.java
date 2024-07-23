package com.woowa.cafe.servlet.qna;

import com.woowa.cafe.dto.SaveArticleDto;
import com.woowa.cafe.service.ArticleService;
import com.woowa.cafe.utils.HttpMessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "articleServlet", urlPatterns = {"/question", "/question/*"})
public class ArticleServlet extends HttpServlet {

    private static final Logger log = getLogger(ArticleServlet.class);

    private ArticleService articleService;

    @Override
    public void init() throws ServletException {
        this.articleService = (ArticleService) getServletContext().getAttribute("articleService");
        log.info("ArticleServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession() == null) {
            resp.sendRedirect("/");
        }

        if(req.getPathInfo() == null) {
            req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp").forward(req, resp);
            return;
        }

        String[] path = req.getPathInfo().split("/");
        Long articleId = Long.parseLong(path[1]);
        req.setAttribute("article", articleService.findById(articleId));
        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp").forward(req, resp);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");
        Map<String, String> bodyFormData = HttpMessageUtils.getBodyFormData(req);
        articleService.save(SaveArticleDto.from(bodyFormData), memberId);

        resp.sendRedirect("/");
    }
}
