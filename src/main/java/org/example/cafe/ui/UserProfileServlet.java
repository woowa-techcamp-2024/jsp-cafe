package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.example.cafe.application.UserService;
import org.example.cafe.domain.User;
import org.slf4j.Logger;

@WebServlet(name = "UserProfileServlet", value = "/users/*")
public class UserProfileServlet extends HttpServlet {

    private static final Logger log = getLogger(UserProfileServlet.class);

    private UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("UserService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 회원 프로필 페이지를 반환한다..
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        String userId = URLDecoder.decode(pathParts[2], StandardCharsets.UTF_8);

        User user = userService.findById(userId);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }
}