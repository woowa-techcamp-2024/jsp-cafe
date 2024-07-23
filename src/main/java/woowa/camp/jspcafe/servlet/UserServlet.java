package woowa.camp.jspcafe.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.service.UserService;

@WebServlet(name = "userServlet", value = "/users")
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("UserServlet init......");
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
        if (this.userService == null) {
            String errorMessage = "[ServletException] UserServlet -> UserService not initialized";
            log.error(errorMessage);
            throw new ServletException(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("userServlet doGet start");

        List<User> users = userService.findAll();
        log.info("users - {}", users);
        req.setAttribute("users", users);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/user/list.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("userServlet doGet end");
    }
}
