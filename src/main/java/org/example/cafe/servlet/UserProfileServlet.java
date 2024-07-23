package org.example.cafe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import org.example.cafe.domain.user.User;
import org.example.cafe.domain.user.UserRepository;

@WebServlet(name = "UserProfileServlet", value = "/users/*")
public class UserProfileServlet extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() {
        this.userRepository = (UserRepository) getServletContext().getAttribute("UserRepository");
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
        String userId = URLDecoder.decode(pathParts[2], "UTF-8");

        User user = userRepository.findById(userId);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }
}