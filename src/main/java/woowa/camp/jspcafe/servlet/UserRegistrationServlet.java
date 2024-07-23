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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.UserRepository;

@WebServlet(name = "userRegistrationServlet", value = "/users/registration")
public class UserRegistrationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationServlet.class);
    private UserRepository userRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.userRepository = (UserRepository) context.getAttribute("userRepository");
        if (this.userRepository == null) {
            throw new ServletException("UserRegistrationServlet -> UserRepository not initialized");
        }
    }

}
