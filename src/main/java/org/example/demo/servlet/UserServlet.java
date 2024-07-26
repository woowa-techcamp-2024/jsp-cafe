package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Router;
import org.example.demo.handler.UserHandler;

@WebServlet(name = "usersServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    private Router router;
    private UserHandler userHandler;

    @Override
    public void init() throws ServletException {
        router = new Router();
        userHandler = UserHandler.getInstance();
        router.addRoute(HttpMethod.GET, "^/users/?$", userHandler::handleUserList);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/?$", userHandler::handleUserProfile);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/form/?$", userHandler::handleUpdateForm);
        router.addRoute(HttpMethod.POST, "^/users/?$", userHandler::handleUserCreate);
        router.addRoute(HttpMethod.POST, "^/users/(\\d+)/?$", userHandler::handleUserUpdate);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (!router.route(request, response)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}