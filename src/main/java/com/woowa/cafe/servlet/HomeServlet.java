package com.woowa.cafe.servlet;

import com.woowa.cafe.dto.article.ArticleListDto;
import com.woowa.cafe.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

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
        List<ArticleListDto> articleDtos = articleService.findAll();
        req.setAttribute("articleDtos", articleDtos);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info("HomeServlet is destroyed");
    }
}
