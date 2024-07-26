package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Router;
import org.example.demo.exception.InternalServerError;
import org.example.demo.handler.PostHandler;

import java.io.IOException;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private Router router;
    private PostHandler postHandler;

    @Override
    public void init() throws ServletException {
        postHandler = PostHandler.getInstance();
        router = new Router();
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/?$", postHandler::handleGetPost);
        router.addRoute(HttpMethod.POST, "^/posts/?$", postHandler::handleCreatePost);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (!router.route(request, response)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}