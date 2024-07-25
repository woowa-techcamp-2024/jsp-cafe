package org.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.entity.User;
import org.example.service.UserService;

@WebServlet("/users/*")
public class UserProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = getPathVariable(request);
        User user = userService.findUserById(userId).orElseThrow(
            () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/profile.jsp");
        dispatcher.forward(request, response);
    }

    private String getPathVariable(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        return pathParts[1];
    }
}
