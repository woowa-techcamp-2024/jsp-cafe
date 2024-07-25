package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;

import java.io.IOException;

@WebServlet(name = "userProfileServlet", urlPatterns = "/users/*")
public class UserProfileServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathAfterUsers = uri.substring((contextPath + "/users/").length());

        System.out.println(pathAfterUsers);
        // Check if the request is for the update form
        if (pathAfterUsers.endsWith("/form")) {
            // Remove "/form" to get the userId
            String userIdStr = pathAfterUsers.substring(0, pathAfterUsers.length() - 5);
            Long id = Long.parseLong(userIdStr);
            User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
            request.setAttribute("user", user);
            request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
        } else {
            // Assume it's a profile view request
            Long id = Long.parseLong(pathAfterUsers);
            User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
            request.setAttribute("user", user);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getRequestURI().substring(request.getContextPath().length() + "/users/".length()));

        // password 검증
        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(request.getParameter("passwordCheck"))) {
            throw new IllegalArgumentException("Password does not match");
        }

        UserDb.updateUser(new UserUpdateDao(
                id,
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        ));

        response.sendRedirect("/users/" + id);
    }
}
