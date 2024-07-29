package camp.woowa.jspcafe.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/user/*"})
public class UserViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if ("/form".equalsIgnoreCase(pathInfo)) {
                req.getRequestDispatcher("/WEB-INF/jsp/user/form.jsp").forward(req, resp);
            } else if ("/login".equalsIgnoreCase(pathInfo)) {
                req.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(req, resp);

            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ServletException | IOException e) {
            log("Failed to forward request", e);
        }

    }
}
