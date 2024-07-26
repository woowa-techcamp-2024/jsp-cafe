package codesquad.javacafe;

import codesquad.javacafe.member.repository.MemberRepository;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.repository.PostRepository;
import codesquad.javacafe.post.service.PostService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * root 요청은 여기에서 핸들링
 */
@WebServlet("")
public class MainController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[MainController] GET request");
        var postList = PostService.getInstance().getAllPosts();
        req.setAttribute("postList",postList);
        var dispatcher = req.getRequestDispatcher("/WEB-INF/index.jsp");
        dispatcher.forward(req, resp);
    }
}
