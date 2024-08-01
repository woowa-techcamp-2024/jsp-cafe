package codesqaud.app.service;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import jakarta.servlet.http.HttpServletRequest;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public abstract class ArticleUseCase {
    protected final ArticleDao articleDao;

    public ArticleUseCase(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    protected Article findArticleByIdOrElseThrow(Long id) {
        return articleDao.findById(id).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );
    }

    protected void authorizeArticle(HttpServletRequest req, Long authorId) {
        if (!AuthenticationManager.isMe(req, authorId)) {
            throw new HttpException(SC_FORBIDDEN, "본인이 작성한 게시글이 아닙니다.");
        }
    }
}
