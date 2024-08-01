package codesquad.service;

import codesquad.domain.article.Article;
import codesquad.domain.article.ArticleDao;
import codesquad.domain.comment.Comment;
import codesquad.domain.comment.CommentDao;
import codesquad.domain.comment.Status;
import codesquad.exception.NoSuchElementException;
import codesquad.exception.UnauthorizedRequestException;

import java.util.List;
import java.util.Optional;

public class DeleteArticleServiceImpl implements DeleteArticleService {
    private ArticleDao articleDao;
    private CommentDao commentDao;

    public DeleteArticleServiceImpl(ArticleDao articleDao, CommentDao commentDao) {
        this.articleDao = articleDao;
        this.commentDao = commentDao;
    }

    @Override
    public void deleteArticle(Command cmd) throws NoSuchElementException, UnauthorizedRequestException {
        long articleId = cmd.articleId();
        String userId = cmd.userId();
        Optional<Article> findArticle = articleDao.findByIdForUpdate(articleId);
        if (findArticle.isEmpty()) {
            throw new NoSuchElementException();
        }
        Article article = findArticle.get();
        article.delete(userId);
        List<Comment> comments = commentDao.findAllByArticleId(articleId);
        for (Comment comment : comments) {
            comment.delete(userId);
        }
        articleDao.update(article);
        commentDao.updateStatus(comments, Status.DELETED);
    }
}
