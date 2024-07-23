package codesquad.jspcafe.domain.article.controller;

import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * ArticleCreateServlet은 질문을 생성하는 요청을 처리하는 서블릿입니다. <br> POST 메서드를 제공하여 새로운 질문을 생성합니다.
 */
@WebServlet("/questions")
public class ArticleCreateServlet extends HttpServlet {

    private transient ArticleService articleService;

    @Override
    public void init() throws ServletException {
        articleService = (ArticleService) getServletContext().getAttribute("articleService");
    }

    /**
     * POST 요청을 처리하여 새로운 아티클을 생성한 후 / 페이지로 리디렉션합니다. 클라이언트가 /questions로 POST 요청을 보낼 때 이 메서드가 호출됩니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 감지될 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();
        articleService.createArticle(map);
        resp.sendRedirect("/");
    }
}
