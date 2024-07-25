package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.db.UserDb;
import org.example.demo.domain.User;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "usersServlet", urlPatterns = "/users/*")
public class UsersServlet extends HttpServlet {

    private static final Pattern LIST_PATTERN = Pattern.compile("^/users/?$");
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^/users/(\\d+)/?$");
    private static final Pattern UPDATE_FORM_PATTERN = Pattern.compile("^/users/(\\d+)/form/?$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        if (LIST_PATTERN.matcher(request.getRequestURI()).matches()) {
            handleUserList(request, response);
        } else if (UPDATE_FORM_PATTERN.matcher(request.getRequestURI()).matches()) {
            handleUpdateForm(request, response);
        } else if (PROFILE_PATTERN.matcher(request.getRequestURI()).matches()) {
            handleUserProfile(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        if (LIST_PATTERN.matcher(request.getRequestURI()).matches()) {
            handleUserCreate(request, response);
        } else if (PROFILE_PATTERN.matcher(request.getRequestURI()).matches()) {
            handleUserUpdate(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("users", UserDb.getUsers());
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }

    private void handleUpdateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = extractIdFromUri(request.getRequestURI(), UPDATE_FORM_PATTERN);
        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
    }

    private void handleUserProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = extractIdFromUri(request.getRequestURI(), PROFILE_PATTERN);
        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    private void handleUserCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreateDao dao = new UserCreateDao(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        UserDb.addUser(dao);
        response.sendRedirect("/users");
    }

    private void handleUserUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = extractIdFromUri(request.getRequestURI(), PROFILE_PATTERN);
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

    private Long extractIdFromUri(String uri, Pattern pattern) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid URI pattern");
    }
}