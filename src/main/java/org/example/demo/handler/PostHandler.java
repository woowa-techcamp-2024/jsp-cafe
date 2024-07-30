package org.example.demo.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Post;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.PostCreateDao;
import org.example.demo.repository.PostRepository;

import java.io.IOException;
import java.util.List;

public class PostHandler {
    private static PostHandler instance;
    private PostRepository postRepository;

    private PostHandler() {
        postRepository = PostRepository.getInstance();
    }

    public static PostHandler getInstance() {
        if (instance == null) {
            instance = new PostHandler();
        }
        return instance;
    }

    public void handleGetPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(postId).orElseThrow(() -> new NotFoundExceptoin("Post not found"));

        request.setAttribute("post", post);
        request.getRequestDispatcher("/post/show.jsp").forward(request, response);
    }

    public void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        String writer = (String) request.getSession().getAttribute("user");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        postRepository.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }

    public void handleEditPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(id).orElseThrow(() -> new NotFoundExceptoin("Post not found"));

        request.setAttribute("post", post);
        request.getRequestDispatcher("/post/edit.jsp").forward(request, response);
    }

    public void handleUpdatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        postRepository.updatePost(id, title, contents);

        response.sendRedirect("/posts/" + id);
    }

    public void handleDeletePost(HttpServletRequest request, HttpServletResponse response, List<String> strings) throws IOException {
        Long id = Long.parseLong(strings.get(0));
        postRepository.deletePost(id);

        response.sendRedirect("/");
    }
}
