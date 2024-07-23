package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = {"/users/update-form/*"})
public class UserProfileUpdateFormView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserProfileUpdateFormView.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("[UserProfileUpdateFormView] called");
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.valueOf(pathParts[pathInfo.length()-1]);
        log.debug("[UserProfileUpdateFormView] "+userId);
        User user = new User(userId, "hong@example.com", "hong", "a", LocalDateTime.now());
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile-update-form.jsp").forward(request, response);
    }
}
