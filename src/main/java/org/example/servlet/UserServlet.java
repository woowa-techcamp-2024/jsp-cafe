package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // 여기서 폼 데이터를 처리하고, 데이터베이스에 저장하거나 다른 작업을 수행할 수 있습니다.

        // 예시로, 콘솔에 폼 데이터 출력
        System.out.println("User ID: " + userId);
        System.out.println("Password: " + password);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);

        // 처리 후 리다이렉션 또는 응답 작성
        response.sendRedirect("/users");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET 요청 처리
        response.getWriter().println("GET 요청은 지원하지 않습니다.");
    }
}

