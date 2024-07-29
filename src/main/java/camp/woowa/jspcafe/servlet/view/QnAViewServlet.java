package camp.woowa.jspcafe.servlet.view;

import camp.woowa.jspcafe.model.Question;
import camp.woowa.jspcafe.service.QuestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/qna/*")
public class QnAViewServlet extends HttpServlet {
    private QuestionService questionService;

    @Override
    public void init() throws ServletException {
        questionService = (QuestionService) getServletContext().getAttribute("questionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.startsWith("/form")) {

            try {
                req.getRequestDispatcher("/WEB-INF/jsp/qna/form.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Exception Occurred", e);
            }
        } else if (pathInfo.startsWith("/show")) {
            String[] split = pathInfo.split("/show/");
            long questionId = 0;
            try {
                questionId = Long.parseLong(split[1]);
            } catch (NumberFormatException e) {
                log("Invalid Question Id", e);
                try {
                    resp.sendRedirect("/");
                } catch (IOException ioException) {
                    log("Exception Occurred", ioException);
                }
            }
            Question question = questionService.findById(questionId);
            req.setAttribute("question", question);
            try {
                req.getRequestDispatcher("/WEB-INF/jsp/qna/show.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                log("Exception Occurred", e);
            }

        }
    }
}
