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
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

@WebServlet("/users/*")
public class UserProfileServlet extends HttpServlet {

    private static final Logger logger =LoggerUtil.getLogger();

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (request.getPathInfo().endsWith("/form")) {
            form(request, response);
            return;
        }

        String userId = getPathVariable(request);
        User user = userService.findUserById(userId).orElseThrow(
            () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/profile.jsp");
        dispatcher.forward(request, response);
    }

    private void form(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String userIdPath = getUserIdPath(request);

        User user = userService.findUserById(userIdPath)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/updateForm.jsp");
        request.setAttribute("user", user);
        dispatcher.forward(request, response);
    }

    private String getPathVariable(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        return pathParts[1];
    }

    private String getUserIdPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] uriArr = uri.split("/");
        return uriArr[uriArr.length - 2];
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = getPathVariable(request);
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        //userService.updateUser(userId, password, name, email);
        response.getWriter().println("사용자 정보를 수정했습니다.");
    }
}
