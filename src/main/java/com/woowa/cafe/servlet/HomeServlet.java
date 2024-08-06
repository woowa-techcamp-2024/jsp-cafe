package com.woowa.cafe.servlet;

import com.woowa.cafe.dto.article.ArticlePageDto;
import com.woowa.cafe.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "homeServlet", urlPatterns = "/")
public class HomeServlet extends HttpServlet {

    private static final Logger log = getLogger(HomeServlet.class);

    private ArticleService articleService;

    @Override
    public void init() {
        this.articleService = (ArticleService) getServletContext().getAttribute("articleService");
        log.info("HomeServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String queryString = req.getQueryString();
        int page = 1;
        int size = 15;
        if (queryString != null) {
            String[] split = queryString.split("&");
            for (String s : split) {
                String[] keyValue = s.split("=");
                if (keyValue[0].equals("page")) {
                    page = Integer.parseInt(keyValue[1]);
                }

                if (keyValue[0].equals("size")) {
                    size = Integer.parseInt(keyValue[1]);
                }
            }
        }
        ArticlePageDto articleDtos = articleService.findByPage(page, size);
        log.info("articleDtos: {}", articleDtos);
        req.setAttribute("articleDtos", articleDtos);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info("HomeServlet is destroyed");
    }
}
