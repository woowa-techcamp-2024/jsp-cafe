package codesquad.jspcafe.domain.article.controller;

import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;


@WebServlet("/questions/list")
public class ArticleListServlet extends HttpServlet {

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

}
