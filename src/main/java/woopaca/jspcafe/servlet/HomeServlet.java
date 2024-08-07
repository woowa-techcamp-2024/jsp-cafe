package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woopaca.jspcafe.error.BadRequestException;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.servlet.dto.response.PostsPageResponse;

import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

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

        String pageParameter = request.getParameter("page");
        if (pageParameter == null) {
            pageParameter = "1";
        }
        int page = Integer.parseInt(pageParameter);
        PostsPageResponse postsPage = postService.getPostsPage(page);
        if (page > postsPage.page().totalPage()) {
            throw new BadRequestException("[ERROR] 페이지가 존재하지 않습니다. 최대 페이지: " + postsPage.page().totalPage());
        }

        request.setAttribute("posts", postsPage.posts());
        request.setAttribute("page", postsPage.page());
        request.setAttribute("postsCount", postsPage.totalPosts());
        request.getRequestDispatcher("/home.jsp")
                .forward(request, response);
    }
}
