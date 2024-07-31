package codesqaud.app.servlet;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import codesqaud.app.model.User;
import codesqaud.app.util.BodyParseUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = {"/qna/*", ""})
public class QnaServlet extends HttpServlet {
    private static final String LIST = "/";
    private static final String BASIC_URI = "/qna";
    private static final String CREATE_FORM_URI = "/qna/form";
    private static final Pattern SPECIFIC_URI_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})$");
    private static final Pattern SPECIFIC_FORM_URI_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})/form$");


    private static final String INDEX_JSP = "/WEB-INF/index.jsp";
    private static final String SHOW_JSP = "/WEB-INF/qna/show.jsp";
    private static final String FORM_JSP = "/WEB-INF/qna/form.jsp";
    private static final String UPDATE_FORM_JSP = "/WEB-INF/qna/update_form.jsp";


    private ArticleDao articleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher;

        if (req.getRequestURI().equals(LIST)) {
            handleArticleList(req, resp);
            return;
        }

        if (req.getRequestURI().equals(CREATE_FORM_URI)) {
            req.getRequestDispatcher(FORM_JSP).forward(req, resp);
            return;
        }

        matcher = SPECIFIC_FORM_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            handleUpdateFormForm(req, resp, id);
            return;
        }


        matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            handleArticleDetails(req, resp, id);
            return;
        }

        throw new HttpException(SC_NOT_FOUND);
    }

    private void handleUpdateFormForm(HttpServletRequest req, HttpServletResponse resp, Long id)
            throws ServletException, IOException {
        Article article = articleDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );
        authorizeArticle(req, article.getAuthorId());

        req.setAttribute("article", article);
        req.getRequestDispatcher(UPDATE_FORM_JSP).forward(req, resp);
    }

    private void handleArticleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ArticleDto> articles = articleDao.findAllAsDto();

        req.setAttribute("articles", articles);
        req.setAttribute("articleSize", articles.size());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(INDEX_JSP);
        requestDispatcher.forward(req, resp);
    }

    private void handleArticleDetails(HttpServletRequest req, HttpServletResponse resp, Long id) throws ServletException, IOException {
        ArticleDto article = articleDao.findByIdAsDto(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );

        req.setAttribute("article", article);
        req.setAttribute("isMine", AuthenticationManager.isMe(req, article.getAuthor().getId()));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(SHOW_JSP);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(BASIC_URI)) {
            handleCreateQna(req, resp);
            return;
        }

        super.doPost(req, resp);
    }

    private void handleCreateQna(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("contents");

        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);
        Article article = new Article(title, content, loginUser.getId());
        articleDao.save(article);

        resp.setStatus(SC_FOUND);
        resp.sendRedirect("/");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            handleArticleUpdate(req, resp, id);
            return;
        }

        super.doPut(req, resp);
    }

    private void handleArticleUpdate(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
        Article article = articleDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );

        authorizeArticle(req, article.getAuthorId());

        processArticleUpdate(req, article);
        resp.sendRedirect("/qna/" + id);
    }

    private void processArticleUpdate(HttpServletRequest req, Article article) throws IOException {
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        article.setTitle(title);
        article.setContents(contents);

        articleDao.update(article);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher;

        matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            handleArticleDelete(req, resp, id);
            return;
        }

        super.doDelete(req, resp);
    }

    private void handleArticleDelete(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
        Article article = findArticleByIdOrElseThrow(id);
        authorizeArticle(req, article.getAuthorId());

        articleDao.delete(article);

        resp.sendRedirect("/");
    }

    private Article findArticleByIdOrElseThrow(Long id) {
        return articleDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );
    }

    private void authorizeArticle(HttpServletRequest req, Long authorId) {
        if (!AuthenticationManager.isMe(req, authorId)) {
            throw new HttpException(SC_FORBIDDEN, "본인이 작성한 게시글이 아닙니다.");
        }
    }
}
