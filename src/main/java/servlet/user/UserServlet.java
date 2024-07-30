package servlet.user;

import dto.UsersDao;
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

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserService userService;
    Logger log = LoggerFactory.getLogger(UserServlet.class);

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Post /users");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        userService.saveUser(new UsersDao(userId, password, name, email));
        resp.sendRedirect("/users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get /users");
        req.setAttribute("users", userService.findAll());
        req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
    }

}