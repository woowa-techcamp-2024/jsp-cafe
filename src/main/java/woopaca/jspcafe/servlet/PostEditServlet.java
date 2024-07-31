package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.servlet.dto.response.PostEditResponse;

import java.io.IOException;

@WebServlet("/posts/edit/*")
public class PostEditServlet extends HttpServlet {

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
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        postService.validateWriter(postId, authentication);
        PostEditResponse postEditResponse = postService.getPostTitleContent(postId);
        request.setAttribute("post", postEditResponse);
        request.getRequestDispatcher("/post/edit.jsp")
                .forward(request, response);
    }
}
