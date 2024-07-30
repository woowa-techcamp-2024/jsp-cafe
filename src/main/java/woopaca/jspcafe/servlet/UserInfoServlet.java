package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woopaca.jspcafe.service.UserService;
import woopaca.jspcafe.servlet.dto.response.UserProfile;

import java.io.IOException;

@WebServlet("/users/*")
public class UserInfoServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(UserInfoServlet.class);

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            Long userId = Long.parseLong(pathInfo.substring(1));
            UserProfile profile = userService.getUserProfile(userId);
            request.setAttribute("profile", profile);
            request.getRequestDispatcher("/user/info.jsp")
                    .forward(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
