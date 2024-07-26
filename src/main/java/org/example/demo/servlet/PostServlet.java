package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.Router;
import org.example.demo.db.PostDb;
import org.example.demo.domain.Post;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.PostCreateDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router();
        router.addRoute(HttpMethod.GET, "^/posts/(\\d+)/?$", this::handleGetPost);
        router.addRoute(HttpMethod.POST, "^/posts/?$", this::handleCreatePost);
        // 추가적인 라우트 (예: 게시글 목록, 수정, 삭제 등)를 여기에 추가할 수 있습니다.
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
        Post post = PostDb.getPost(postId).orElseThrow(() -> new NotFoundExceptoin("Post not found"));

        request.setAttribute("post", post);
        request.getRequestDispatcher("/post/show.jsp").forward(request, response);
    }

    private void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        PostDb.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }
}