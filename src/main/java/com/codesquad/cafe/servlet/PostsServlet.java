package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.InMemoryPostRepository;
import com.codesquad.cafe.db.InMemoryUserRepository;
import com.codesquad.cafe.model.Post;
import com.codesquad.cafe.model.PostDetailsDto;
import com.codesquad.cafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PostsServlet.class);

    private InMemoryPostRepository postRepository;

    private InMemoryUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (InMemoryPostRepository) getServletContext().getAttribute("postRepository");
        this.userRepository = (InMemoryUserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long postId = Long.parseLong(req.getPathInfo().substring(1));
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                resp.sendError(404);
                return;
            }
            PostDetailsDto postDetail = getPostDetailsByJoin(post.get());
            req.setAttribute("post", postDetail);
            req.getRequestDispatcher("/WEB-INF/views/post_detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        } catch (NullPointerException e) {
            resp.sendError(500);
        }
    }

    private PostDetailsDto getPostDetailsByJoin(Post post) {
        Optional<User> user = userRepository.findById(post.getAuthorId());
        return new PostDetailsDto(post, user.get());
    }
}
