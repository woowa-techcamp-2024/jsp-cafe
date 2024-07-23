package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "usersServlet", urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
    // 회원 목록 조회
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        request.setAttribute("users", UserDb.getUsers());

        request.getRequestDispatcher("/user/list.jsp").forward(request, response);

        System.out.println("do get /users");
    }

    // 회원가입
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));

        UserDb.addUser(user);

        response.sendRedirect("/users");
        System.out.println("do post /users");
    }
}
