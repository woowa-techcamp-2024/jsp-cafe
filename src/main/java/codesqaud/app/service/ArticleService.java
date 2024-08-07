package codesqaud.app.service;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.db.TransactionManager;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.dto.PageDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

public class ArticleService {
    private static final String INDEX_JSP = "/WEB-INF/index.jsp";
    private static final String SHOW_JSP = "/WEB-INF/qna/show.jsp";
    private static final String FORM_JSP = "/WEB-INF/qna/form.jsp";
    private static final String UPDATE_FORM_JSP = "/WEB-INF/qna/update_form.jsp";

    private final ArticleDao articleDao;
    private final ReplyDao replyDao;
    private final DataSource dataSource;

    public ArticleService(ArticleDao articleDao, ReplyDao replyDao, DataSource dataSource) {
        this.articleDao = articleDao;
        this.replyDao = replyDao;
        this.dataSource = dataSource;
    }

    public void handleArticleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParameter = req.getParameter("page");
        int page = 0;
        if (pageParameter != null) {
            page = Integer.parseInt(pageParameter) - 1;
        }

        String sizeParameter = req.getParameter("size");
        int size = 15;
        if (sizeParameter != null) {
            size = Math.min(Integer.parseInt(sizeParameter), 30);
        }

        long totalArticleCount = articleDao.count();
        List<ArticleDto> articles = articleDao.findPage(page, size);
        int totalPageCount = (int) Math.ceil((double) totalArticleCount / size);

        PageDto<ArticleDto> articlePage = PageDto.<ArticleDto>builder()
                .elements(articles)
                .totalElementsCount(totalArticleCount)
                .totalPage(totalPageCount)
                .build();


        req.setAttribute("articlePage", articlePage);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(INDEX_JSP);
        requestDispatcher.forward(req, resp);
    }

    public void handleArticleCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(FORM_JSP).forward(req, resp);
    }

    public void handleUpdateForm(HttpServletRequest req, HttpServletResponse resp, Long id)
            throws ServletException, IOException {
        Article article = articleDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );
        authorizeArticle(req, article.getAuthorId());

        req.setAttribute("article", article);
        req.getRequestDispatcher(UPDATE_FORM_JSP).forward(req, resp);
    }

    public void handleArticleDetails(HttpServletRequest req, HttpServletResponse resp, Long id) throws ServletException, IOException {
        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);

        ArticleDto article = articleDao.findByIdAsDto(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );
        article.setMine(loginUser.getId());
        req.setAttribute("article", article);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(SHOW_JSP);
        requestDispatcher.forward(req, resp);
    }

    /*
        POST Handlers
     */
    public void handleCreateQna(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("contents");

        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);
        Article article = new Article(title, content, loginUser.getId());
        articleDao.save(article);

        resp.setStatus(SC_FOUND);
        resp.sendRedirect("/");
    }

    /*
        PUT Handlers
     */
    public void handleArticleUpdate(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
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

    /*
        DELETE HANDLERS
     */
    public void handleArticleDeletion(HttpServletRequest req, HttpServletResponse resp, Long id) throws IOException {
        TransactionManager.executeTransaction(dataSource, () -> handleArticleDeletion(req, id));
        resp.sendRedirect("/");
    }

    private void handleArticleDeletion(HttpServletRequest req, Long id) {
        Article article = articleDao.findByIdForUpdate(id)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND));

        List<Reply> replies = replyDao.findByArticleId(article.getId());

        authorizeArticle(req, article.getAuthorId());
        validateDelete(req, replies);

        articleDao.delete(article);
        replies.forEach(replyDao::delete);
    }

    private void validateDelete(HttpServletRequest req, List<Reply> replies) {
        boolean hasOthersReplies = false;
        for (Reply reply : replies) {
            if (!AuthenticationManager.isMe(req, reply.getAuthorId())) {
                hasOthersReplies = true;
                break;
            }
        }

        if (hasOthersReplies) {
            throw new HttpException(SC_FORBIDDEN, "다른 사람이 작성한 댓글이 있습니다.");
        }
    }

    private void authorizeArticle(HttpServletRequest req, Long authorId) {
        if (!AuthenticationManager.isMe(req, authorId)) {
            throw new HttpException(SC_FORBIDDEN, "본인이 작성한 게시글이 아닙니다.");
        }
    }
}
