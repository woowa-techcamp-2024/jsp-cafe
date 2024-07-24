package codesquad.servlet;

import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.servlet.dto.ArticleResponse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    private ArticleQueryDao articleQueryDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        articleQueryDao = (ArticleQueryDao) servletContext.getAttribute("articleQueryDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ArticleResponse> articleResponses = articleQueryDao.findAll();
        req.setAttribute("articleResponses", articleResponses);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
