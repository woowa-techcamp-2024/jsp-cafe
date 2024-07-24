package camp.woowa.jspcafe.servlets;

import camp.woowa.jspcafe.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error")
public class GlobalExceptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomException exception = (CustomException) req.getAttribute("camp.woowa.jspcafe.exception.CustomException");

        req.setAttribute("exception", exception);

        try {
            req.getRequestDispatcher("/WEB-INF/jsp/error/error.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log("Failed to forward request", e);
        }
    }
}
