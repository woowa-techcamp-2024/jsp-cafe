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
import woopaca.jspcafe.resolver.RequestParametersResolver;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.servlet.dto.request.WritePostRequest;

import java.io.IOException;
import java.util.Map;

@WebServlet("/posts/write")
public class PostWriteServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.postService = (PostService) servletContext.getAttribute("postService");
    }

    /**
     * 게시글 작성 폼 요청을 처리
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/post/post-form.jsp")
                .forward(request, response);
    }

    /**
     * 게시글 작성 HTTP POST 요청을 처리
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> parameters = request.getParameterMap();
        WritePostRequest writePostRequest = RequestParametersResolver.resolve(parameters, WritePostRequest.class);
        HttpSession session = request.getSession();
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        postService.writePost(writePostRequest, authentication);
        response.sendRedirect("/");
    }
}
