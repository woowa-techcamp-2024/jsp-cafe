package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woopaca.jspcafe.error.NotFoundException;
import woopaca.jspcafe.service.UserService;
import woopaca.jspcafe.servlet.dto.response.UserProfile;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/users/*")
public class UserInfoServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Pattern pattern = Pattern.compile("[^0-9/]");
        if (pattern.matcher(pathInfo).find()) {
            throw new NotFoundException("[ERROR] 잘못된 경로입니다.");
        }

        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long userId = Long.parseLong(pathInfo.substring(1));
        UserProfile profile = userService.getUserProfile(userId);
        request.setAttribute("profile", profile);
        request.getRequestDispatcher("/user/info.jsp")
                .forward(request, response);
    }
}
