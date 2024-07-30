package servlet.article;

import domain.Article;
import domain.User;
import dto.ArticleDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;
import utils.AuthUtils;

import java.io.IOException;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {

    private ArticleService articleService;
    Logger log = LoggerFactory.getLogger(QuestionServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    // 1. 미로그인시 로그인 페이지로 이동
    // 2. question?id=5 형태의 경우 id에 해당하는 게시글을 보여준다.
    // 3. 없을 경우 새로운 게시글을 작성할 수 있는 form을 보여준다.
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        HttpSession session = req.getSession(false);
        if (!AuthUtils.isLoginUser(session)) {
            resp.sendRedirect("/user/login.jsp");
            return;
        }

        if (id != null) {
            Article article = articleService.findById(Long.parseLong(id));
            req.setAttribute("article", article);
            req.getRequestDispatcher("/qna/show.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("/qna/form.jsp").forward(req, resp);
    }

    @Override
    // 1. 미로그인시 로그인 페이지로 이동
    // 2. 게시글 작성 후 /로 이동
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!AuthUtils.isLoginUser(session)) {
            resp.sendRedirect("/user/login.jsp");
            return;
        }
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        log.info("{} {}", title, content);
        articleService.saveArticle(new ArticleDao((User) session.getAttribute("loginMember"), title, content));
        resp.sendRedirect("/");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Question Servlet doPut");
        log.info("{}", req.getParameter("id"));
    }

}
