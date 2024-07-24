package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "userProfileServlet", urlPatterns = "/users/*")
public class UserProfileServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        Long id = Long.valueOf(request.getRequestURI().substring(request.getContextPath().length() + "/users/".length()));

        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }
}
