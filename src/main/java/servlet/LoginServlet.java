package servlet;

import domain.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.IOException;

@WebServlet("/users/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    Logger log = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        if(userService.isValidUser(userId, password)) {
            User user = userService.findByUserId(userId);
            HttpSession session = req.getSession();
            // jsession으로 구별한다.
            session.setAttribute("loginMember", user);
            session.setMaxInactiveInterval(60 * 2);
            resp.sendRedirect("/users");
        } else {
            resp.sendRedirect("/user/login_failed.jsp");
        }
    }
}
