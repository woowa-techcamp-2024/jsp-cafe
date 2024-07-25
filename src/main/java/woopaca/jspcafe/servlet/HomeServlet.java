package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.servlet.dto.response.PostsResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(HomeServlet.class);

    private PostService postService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.postService = (PostService) servletContext.getAttribute("postService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath != null && !servletPath.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<PostsResponse> posts = postService.getAllPosts();
        request.setAttribute("posts", posts);
        request.setAttribute("postsCount", posts.size());
        request.getRequestDispatcher("/home.jsp")
                .forward(request, response);
    }
}
