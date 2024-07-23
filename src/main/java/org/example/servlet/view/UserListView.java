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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users")
public class UserListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserListView.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("[UserListView] called");
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "hong@example.com", "hong", "a", LocalDateTime.now()));
        users.add(new User(2L, "kim@example.com", "kim",  "a", LocalDateTime.now()));
        users.add(new User(3L, "lee@example.com", "lee", "a", LocalDateTime.now()));

        request.setAttribute("users", users);
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }
}
