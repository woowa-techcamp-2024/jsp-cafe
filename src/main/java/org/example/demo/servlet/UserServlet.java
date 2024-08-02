package org.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.exception.InternalServerError;
import org.example.demo.handler.UserHandler;
import org.example.demo.router.Router;

import java.io.IOException;

@WebServlet(name = "usersServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    private Router router;
    private UserHandler userHandler;

    @Override
    public void init() {
        router = new Router();
        userHandler = (UserHandler) getServletContext().getAttribute("userHandler");
        router.addRoute(HttpMethod.GET, "^/users/?$", userHandler::handleUserList);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/?$", userHandler::handleUserProfile);
        router.addRoute(HttpMethod.GET, "^/users/(\\d+)/form/?$", userHandler::handleUpdateForm);
        router.addRoute(HttpMethod.POST, "^/users/?$", userHandler::handleUserCreate);
        router.addRoute(HttpMethod.POST, "^/users/(\\d+)/?$", userHandler::handleUserUpdate);
        router.addRoute(HttpMethod.POST, "^/users/login/?$", userHandler::handleUserLogin);
        router.addRoute(HttpMethod.POST, "^/users/logout/?$", userHandler::handleUserLogout);
        router.addRoute(HttpMethod.GET, "^/users/login/?$", userHandler::handleUserLoginPage);
        router.addRoute(HttpMethod.GET, "^/users/form/?$", userHandler::handleUserFormPage);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!router.route(request, response)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}