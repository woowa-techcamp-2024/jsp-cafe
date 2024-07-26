package servlet;

import domain.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.IOException;


@WebServlet("/users/*")
public class ProfileServlet extends HttpServlet {

    private UserService userService;
    Logger log = LoggerFactory.getLogger(CreateAccountServlet.class);

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ProfileServlet doGet");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.parseLong(pathParts[1]);

        User user = userService.findById(userId);
        req.setAttribute("user", user);

        if (pathParts.length != 2) {
            req.getRequestDispatcher("/user/updateForm.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ProfileServlet doPost");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        log.info("userId: {}, password: {}, newPassword: {}, name: {}, email: {}", userId, password, newPassword, name, email);
        userService.changeProfile(userId, newPassword, name, email, password);
        resp.sendRedirect("/users");
    }
}
