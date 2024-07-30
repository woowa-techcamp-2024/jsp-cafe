package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Route;
import org.example.demo.Router;
import org.example.demo.aop.AopValidator;
import org.example.demo.exception.InternalServerError;
import org.example.demo.handler.PostHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private Router router;
    private PostHandler postHandler;

    @Override
    public void init() {
        postHandler = PostHandler.getInstance();
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/?$", postHandler::handleGetPost);
        router.addRoute(HttpMethod.POST, "^/posts/?$", postHandler::handleCreatePost);
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/edit/?$", postHandler::handleEditPost);
        router.addRoute(HttpMethod.PUT, "^/posts/(\\d+)/?$", postHandler::handleUpdatePost);
        router.addRoute(HttpMethod.DELETE, "^/posts/(\\d+)/?$", postHandler::handleDeletePost);
        router.addRoute(HttpMethod.GET, "^/posts/form/?$", postHandler::handlePostForm);
        router = new Router(new AopValidator());
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