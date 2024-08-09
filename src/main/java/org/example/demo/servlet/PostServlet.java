package org.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.exception.InternalServerError;
import org.example.demo.handler.CommentHandler;
import org.example.demo.handler.PostHandler;
import org.example.demo.router.Router;

import java.io.IOException;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private Router router;
    private PostHandler postHandler;
    private CommentHandler commentHandler;

    @Override
    public void init() {
        postHandler = (PostHandler) getServletContext().getAttribute("postHandler");
        commentHandler = (CommentHandler) getServletContext().getAttribute("commentHandler");
        router = new Router();
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/?$", postHandler::handleGetPost);
        router.addRoute(HttpMethod.POST, "^/posts/?$", postHandler::handleCreatePost);
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/edit/?$", postHandler::handleEditPost);
        router.addRoute(HttpMethod.PUT, "^/posts/(\\d+)/?$", postHandler::handleUpdatePost);
        router.addRoute(HttpMethod.DELETE, "^/posts/(\\d+)/?$", postHandler::handleDeletePost);
        router.addRoute(HttpMethod.GET, "^/posts/form/?$", postHandler::handlePostForm);
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/comments/?$", commentHandler::getComments);
        router.addRoute(HttpMethod.POST, "^/posts/(\\d+)/comments/?$", commentHandler::createComment);
        router.addRoute(HttpMethod.DELETE, "^/posts/(\\d+)/comments/(\\d+)/?$", commentHandler::deleteComment);
        router.addRoute(HttpMethod.PUT, "^/posts/(\\d+)/comments/(\\d+)/?$", commentHandler::updateComment);
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