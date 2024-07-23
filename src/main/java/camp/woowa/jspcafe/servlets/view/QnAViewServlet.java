package camp.woowa.jspcafe.servlets.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/qna/form")
public class QnAViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            req.getRequestDispatcher("/WEB-INF/jsp/qna/form.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log("Exception Occurred", e);
        }
    }
}
