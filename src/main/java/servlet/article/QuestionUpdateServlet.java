package servlet.article;

import domain.Article;
import domain.User;
import exception.TomcatException;
import exception.UnAuthorizedException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;
import utils.AuthUtils;

import java.io.IOException;
import java.util.Map;

@WebServlet("/questions/*")
public class QuestionUpdateServlet extends HttpServlet {

    private ArticleService articleService;
    Logger log = LoggerFactory.getLogger(QuestionUpdateServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("QuestionUpdateServlet doGet");
        HttpSession session = req.getSession(false);
        Long articleId = parseArticleId(req.getPathInfo());
        Article article = articleService.findById(articleId);

        // 로그인 여부 확인
        // 남의 게시글인지 확인(인가)
        AuthUtils.checkLogin(session);
        checkAuthorization(session, article ,"수정");

        req.setAttribute("article", article);
        req.getRequestDispatcher("/qna/updateForm.jsp").forward(req, resp);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("QuestionUpdateServlet doPut");

        HttpSession session = req.getSession(false);
        Long articleId = parseArticleId(req.getPathInfo());
        Article article = articleService.findById(articleId);

        // 로그인 여부 확인
        // 남의 게시글인지 확인(인가)
        AuthUtils.checkLogin(session);
        checkAuthorization(session, article, "삭제");

        JSONParser parser = new JSONParser(req.getReader());
        try {
            Object jsonObject = parser.parse();
            if(jsonObject instanceof Map) {
                Map<String, String> jsonMap = (Map<String, String>) jsonObject;
                log.info("jsonOb: {}, {}", jsonMap.get("title"), jsonMap.get("content"));

                String title = jsonMap.get("title");
                String content = jsonMap.get("content");
                articleService.updateArticle(article, title, content);
            } else {
                throw new TomcatException("잘못된 요청입니다.");
            }
        } catch (ParseException e) {
            throw new TomcatException(e.getMessage());
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        log.info("QuestionUpdateServlet doDelete");

        HttpSession session = req.getSession(false);
        Long articleId = parseArticleId(req.getPathInfo());
        Article article = articleService.findById(articleId);

        // 로그인 여부 확인
        // 남의 게시글인지 확인(인가)
        AuthUtils.checkLogin(session);
        checkAuthorization(session, article, "삭제");

        articleService.deleteArticle(articleId);
        log.info("QuestionUpdateServlet doDelete end");

    }

    private void checkAuthorization(HttpSession session, Article article, String action) {
        User loginUser = (User) session.getAttribute(AuthUtils.LOGIN_MEMBER);
        // 게시글 작성자와 로그인한 사용자가 다르면 예외를 반환한다.
        if (!article.getWriter().getUserId().equals(loginUser.getUserId())) {
            log.info("auth fail");
            throw new UnAuthorizedException("다른 사용자의 게시글을 " + action + "할 수 없습니다.");
        }
    }

    private Long parseArticleId(String pathInfo) {
        String[] pathParts = pathInfo.split("/");
        return Long.parseLong(pathParts[1]);
    }

}
