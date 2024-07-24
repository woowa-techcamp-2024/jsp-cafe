package cafe.controller;

import cafe.service.ArticleService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "articleServlet", value = "/articles/*")
public class ArticleServlet extends HttpServlet {
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setAttribute("article", articleService.find(req, resp));
            RequestDispatcher dispatcher = req.getRequestDispatcher("/qna/show.jsp");
            dispatcher.forward(req, resp);
        } catch (IOException | ServletException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            articleService.save(req, resp);
            resp.sendRedirect("/");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
