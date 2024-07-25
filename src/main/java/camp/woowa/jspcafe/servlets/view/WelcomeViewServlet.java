package camp.woowa.jspcafe.servlets.view;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.services.QuestionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "")
public class WelcomeViewServlet extends HttpServlet {
    private QuestionService questionService;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();

        questionService = (QuestionService) sc.getAttribute("questionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (("".equalsIgnoreCase(pathInfo) || "/".equalsIgnoreCase(pathInfo))) {
            try {
                req.setAttribute("questions", questionService.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Error", e);
            }
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }
}
