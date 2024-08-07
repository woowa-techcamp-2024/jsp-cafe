package codesqaud.app.servlet;

import codesqaud.app.exception.HttpException;
import codesqaud.app.service.ArticleService;
import codesqaud.app.service.ReplyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(urlPatterns = {"/qna/*", ""})
public class QnaServlet extends HttpServlet {
    private static final String LIST = "/";
    private static final String BASIC_URI = "/qna";
    private static final String CREATE_FORM_URI = "/qna/form";
    private static final Pattern SPECIFIC_URI_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})$");
    private static final Pattern SPECIFIC_FORM_URI_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})/form$");
    private static final Pattern REPLY_BASE_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})/replies$");
    private static final Pattern SPECIFIC_REPLY_PATTERN = Pattern.compile("^/qna/([1-9][\\d]{0,9})/replies/([1-9][\\d]{0,9})$");

    private static final String FORM_JSP = "/WEB-INF/qna/form.jsp";

    private ArticleService articleService;
    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.articleService = (ArticleService) config.getServletContext().getAttribute("articleService");
        this.replyService = (ReplyService) config.getServletContext().getAttribute("replyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher;

        if (req.getRequestURI().equals(LIST)) {
            articleService.handleArticleList(req, resp);
            return;
        }

        if (req.getRequestURI().equals(CREATE_FORM_URI)) {
            articleService.handleArticleCreateForm(req, resp);
            return;
        }

        matcher = SPECIFIC_FORM_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            articleService.handleUpdateForm(req, resp, id);
            return;
        }

        matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            articleService.handleArticleDetails(req, resp, id);
            return;
        }

        matcher = REPLY_BASE_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long articleId = Long.parseLong(matcher.group(1));
            replyService.handleGetReplies(req, resp, articleId);
            return;
        }

        throw new HttpException(SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher;
        if (req.getRequestURI().equals(BASIC_URI)) {
            articleService.handleCreateQna(req, resp);
            return;
        }

        matcher = REPLY_BASE_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long articleId = Long.parseLong(matcher.group(1));
            replyService.handleReplyCreation(req, resp, articleId);
            return;
        }

        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            articleService.handleArticleUpdate(req, resp, id);
            return;
        }

        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matcher matcher;

        matcher = SPECIFIC_URI_PATTERN.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            articleService.handleArticleDeletion(req, resp, id);
            return;
        }

        matcher = SPECIFIC_REPLY_PATTERN.matcher(req.getRequestURI());
        if(matcher.matches()) {
            Long articleId = Long.parseLong(matcher.group(1));
            Long replyId = Long.parseLong(matcher.group(2));
            replyService.handle(req, resp, articleId, replyId);
            return;
        }

        super.doDelete(req, resp);
    }
}
