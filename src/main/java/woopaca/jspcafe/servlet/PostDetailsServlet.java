package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.servlet.dto.PostDetailsResponse;

import java.io.IOException;

@WebServlet("/posts/*")
public class PostDetailsServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.postService = (PostService) servletContext.getAttribute("postService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long postId = Long.parseLong(pathInfo.substring(1));
        PostDetailsResponse post = postService.getPostDetails(postId);
        request.setAttribute("post", post);
        request.setAttribute("separator", '\n');
        request.getRequestDispatcher("/post/details.jsp")
                .forward(request, response);
    }
}
