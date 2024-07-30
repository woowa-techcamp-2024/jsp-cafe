package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.example.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();

        System.out.println("User ID: " + userId);
        System.out.println("Password: " + password);

        if (userService.login(userId, password)) {
            // 로그인 성공
            // 세션에 사용자 정보 저장
            request.getSession().setAttribute("userId", userId);
            // 사용자 프로필 페이지로 리다이렉션
            response.sendRedirect("/");
        } else {
            // 로그인 실패
            // 로그인 페이지로 리다이렉션
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // GET 요청 처리
        // 로그인 페이지로 포워드
        request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
    }
}
