package servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;

import java.io.IOException;

@WebServlet("")
public class MainPageServlet extends HttpServlet {

    private ArticleService articleService;
    Logger log = LoggerFactory.getLogger(MainPageServlet.class);

    @Override
    public void init() {
        ServletContext context = getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("MainPageServlet doGet");
        req.setAttribute("articles", articleService.findAll());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

}
