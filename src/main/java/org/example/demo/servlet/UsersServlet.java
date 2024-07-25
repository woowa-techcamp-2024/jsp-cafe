package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Router;
import org.example.demo.db.UserDb;
import org.example.demo.domain.User;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "usersServlet", urlPatterns = "/users/*")
public class UsersServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router();
        router.addRoute(HttpMethod.GET, "^/users/?$", this::handleUserList);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/?$", this::handleUserProfile);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/form/?$", this::handleUpdateForm);
        router.addRoute(HttpMethod.POST, "^/users/?$", this::handleUserCreate);
        router.addRoute(HttpMethod.POST, "^/users/(\\d+)/?$", this::handleUserUpdate);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (!router.route(request, response)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleUserList(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.setAttribute("users", UserDb.getUsers());
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }

    private void handleUpdateForm(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
    }

    private void handleUserProfile(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        User user = UserDb.getUser(id).orElseThrow(() -> new RuntimeException("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    private void handleUserCreate(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        UserCreateDao dao = new UserCreateDao(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        UserDb.addUser(dao);
        response.sendRedirect("/users");
    }

    private void handleUserUpdate(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        Long id = Long.parseLong(pathVariables.get(0));
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