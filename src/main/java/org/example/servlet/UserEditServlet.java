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

@WebServlet("/users/*/form")
public class UserEditServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // 사용자 수정 폼 요청 처리
        String userIdPath = getUserIdPath(request);

        User user = userService.findUserById(userIdPath)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        request.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/updateForm.jsp");
        dispatcher.forward(request, response);
    }

    private String getUserIdPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] uriArr = uri.split("/");
        return uriArr[uriArr.length - 2];
    }

}
