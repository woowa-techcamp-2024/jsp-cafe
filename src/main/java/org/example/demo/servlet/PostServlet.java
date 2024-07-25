package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Router;
import org.example.demo.db.DbConfig;
import org.example.demo.domain.Post;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.PostCreateDao;
import org.example.demo.repository.PostRepository;
import org.example.demo.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private Router router;
    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        router = new Router();
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/?$", this::handleGetPost);
        router.addRoute(HttpMethod.POST, "^/posts/?$", this::handleCreatePost);
        DbConfig dbConfig = new DbConfig("jdbc:mysql://localhost/test", "root", "");
        postRepository = new PostRepository(dbConfig, new UserRepository(dbConfig));
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

    private void handleGetPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(postId).orElseThrow(() -> new NotFoundExceptoin("Post not found"));

        request.setAttribute("post", post);
        request.getRequestDispatcher("/post/show.jsp").forward(request, response);
    }

    private void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        postRepository.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }
}