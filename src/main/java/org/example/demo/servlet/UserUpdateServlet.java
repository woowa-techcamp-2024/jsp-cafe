package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.db.UserDb;
import org.example.demo.domain.User;

import java.io.IOException;

//@WebServlet(name = "userUpdateServlet", urlPatterns = "/users/*/form")
public class UserUpdateServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("do get /users/*/form");
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathAfterUsers = uri.substring((contextPath + "/users/").length());
        System.out.println(pathAfterUsers);
        System.out.println(pathAfterUsers.split("/")[0]);
        Long id = Long.parseLong(pathAfterUsers.split("/")[0]);

        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
    }
}
