package codesquad.jspcafe.domain.article.controller;

import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;


/**
 * ArticleServlet은 질문 조회 요청을 처리하는 서블릿입니다. <br> GET 메서드를 제공하여 질문을 표시합니다.
 */
@WebServlet("/questions/*")
public class ArticleServlet extends HttpServlet {

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
     * GET 요청을 처리하여 question.jsp 페이지로 포워딩합니다. <br> 클라이언트가 /questions/{articleId}로 GET 요청을 보낼 때 이
     * 메서드가 호출됩니다. <br> articleId가 존재하지 않는 경우 NoSuchElementException{@link NoSuchElementException}
     * 예외를 던집니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      포워드 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        verifyPathInfo(pathInfo);
        String articleId = pathInfo.substring(1);
        ArticleCommonResponse articleCommonResponse = articleService.getArticleById(
            articleId);
        req.setAttribute("article", articleCommonResponse);
        req.getRequestDispatcher("/WEB-INF/jsp/question.jsp").forward(req, resp);
    }

    private void verifyPathInfo(String pathInfo) {
        if (pathInfo == null || pathInfo.isBlank()) {
            throw new NoSuchElementException("아티클을 찾을 수 없습니다.");
        }
    }
}
