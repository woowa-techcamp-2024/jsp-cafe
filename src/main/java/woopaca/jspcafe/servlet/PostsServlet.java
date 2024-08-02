package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.resolver.QueryStringResolver;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.service.ReplyService;
import woopaca.jspcafe.servlet.dto.request.PostEditRequest;
import woopaca.jspcafe.servlet.dto.response.PostDetailsResponse;
import woopaca.jspcafe.servlet.dto.response.RepliesResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet("/posts/*")
public class PostsServlet extends HttpServlet {

    private PostService postService;
    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.postService = (PostService) servletContext.getAttribute("postService");
        this.replyService = (ReplyService) servletContext.getAttribute("replyService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long postId = Long.parseLong(pathInfo.substring(1));
        request.setAttribute("postId", postId);
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long postId = (Long) request.getAttribute("postId");
        PostDetailsResponse post = postService.getPostDetails(postId);
        List<RepliesResponse> replies = replyService.getReplies(postId);
        request.setAttribute("post", post);
        request.setAttribute("separator", '\n');
        request.setAttribute("replies", replies);
        request.getRequestDispatcher("/post/details.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long postId = (Long) request.getAttribute("postId");
        String queryString = request.getReader()
                .lines()
                .collect(Collectors.joining());
        PostEditRequest postEditRequest = QueryStringResolver.resolve(queryString, PostEditRequest.class);

        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        postService.updatePost(postId, postEditRequest, authentication);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Long postId = (Long) request.getAttribute("postId");
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        postService.deletePost(postId, authentication);
    }
}
