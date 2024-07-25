package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.db.UserDb;
import org.example.demo.domain.User;
import org.example.demo.model.UserCreateDao;

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
                null,
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));

        UserCreateDao dao = new UserCreateDao(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        UserDb.addUser(dao);

        response.sendRedirect("/users");
        System.out.println("do post /users");
    }
}
