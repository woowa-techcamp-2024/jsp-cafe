package codesquad.jspcafe.domain.article.controller;

import codesquad.jspcafe.domain.article.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ArticleListServlet는 Article 리스트 조회를 처리하는 서블릿입니다. <br> GET 요청을 처리하여 Article 리스트를 조회한 후 JSON 바디를
 * 반환합니다. <br> 이 서블릿은 /questions/list 경로로 매핑되어 있습니다.
 */
@WebServlet("/questions/list")
public class ArticleListServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private transient ArticleService articleService;

    /**
     * 서블릿을 초기화하여 서블릿 컨텍스트에서 ArticleService를 가져옵니다. 이 메서드는 서블릿이 처음 로드될 때 서블릿 컨테이너에 의해 호출됩니다.
     *
     * @throws ServletException 초기화 중 오류가 발생할 경우
     */
    @Override
    public void init() throws ServletException {
        articleService = (ArticleService) getServletContext().getAttribute("articleService");
    }

    /**
     * GET 요청을 처리하여 Article 리스트를 조회한 후 JSON 바디를 반환합니다. <br> 클라이언트가 /questions/list로 GET 요청을 보낼 때 이
     * 메서드가 호출됩니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String parameter = req.getParameter("id");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(
            parameter == null ? articleService.getArticleKeys()
                : articleService.getArticlesById(Long.parseLong(parameter))));
    }
}
