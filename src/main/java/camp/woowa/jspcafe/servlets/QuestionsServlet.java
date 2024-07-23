package camp.woowa.jspcafe.servlets;

import camp.woowa.jspcafe.services.QuestionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/questions")
public class QuestionsServlet extends HttpServlet {
    private QuestionService questionService;
    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();

        questionService = (QuestionService) sc.getAttribute("questionService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String writer = req.getParameter("writer");

        questionService.save(title, content, writer);

        try {
            resp.sendRedirect("/");
        } catch (IOException e) {
            log("Redirect Error", e);
        }
    }
}
