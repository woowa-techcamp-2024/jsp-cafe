package org.example.servlet.view;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.constance.DataHandler;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("")
public class ArticlesListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticlesListView.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("[ArticlesListView] called");
        int page = getPageNumber(request);
        List<Article> articles = articleDataHandler.findByPage(page);
        int totalPageNumber = articleDataHandler.getTotalPageNumber();
        request.setAttribute("articles", articles);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPageNumber", totalPageNumber);
        request.getRequestDispatcher("/article/list.jsp").forward(request, response);
    }

    private int getPageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                int page = Integer.parseInt(pageParam);
                if (page > 0) {
                    return page;
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid page number: {}", pageParam);
            }
        }
        return 1; // 기본값은 1페이지
    }
}