package com.woowa.cafe.servlet.qna;

import com.woowa.cafe.dto.article.ArticleDto;
import com.woowa.cafe.dto.article.SaveArticleDto;
import com.woowa.cafe.exception.HttpException;
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
    public void init() {
        this.articleService = (ArticleService) getServletContext().getAttribute("articleService");
        log.info("ArticleServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession() == null) {
            resp.sendRedirect("/");
        }

        if (req.getPathInfo() == null) {
            req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp").forward(req, resp);
            return;
        }

        String[] path = req.getPathInfo().split("/");
        Long articleId = Long.parseLong(path[1]);

        if (path.length == 3 && path[2].equals("form")) {
            ArticleDto article = articleService.findById(articleId);
            if (checkWriter(req, resp, article)) {
                req.setAttribute("article", article);
                req.getRequestDispatcher("/WEB-INF/views/qna/updateForm.jsp").forward(req, resp);
                return;
            }
            throw new HttpException(HttpServletResponse.SC_FORBIDDEN, "다른 사람이 수정할 수 없습니다.");
        }

        req.setAttribute("article", articleService.findById(articleId));
        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp").forward(req, resp);
    }

    private boolean checkWriter(final HttpServletRequest req, final HttpServletResponse resp, final ArticleDto article) throws ServletException, IOException {
        return article.writerId().equals(req.getSession().getAttribute("memberId"));
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");
        Map<String, String> bodyFormData = HttpMessageUtils.getBodyFormData(req);
        articleService.save(SaveArticleDto.from(bodyFormData), memberId);

        resp.sendRedirect("/");
    }

    @Override
    public void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.info(req.toString());

        String[] path = req.getPathInfo().split("/");
        Long articleId = Long.parseLong(path[1]);

        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");
        Map<String, String> bodyFormData = HttpMessageUtils.getBodyFormData(req);
        log.info("bodyFormData: {}", bodyFormData.toString());
        articleService.update(articleId, SaveArticleDto.from(bodyFormData), memberId);

        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/question/" + articleId);
    }

    @Override
    public void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String[] path = req.getPathInfo().split("/");
        Long articleId = Long.parseLong(path[1]);

        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");

        articleService.delete(articleId, memberId);

        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/");
    }
}
