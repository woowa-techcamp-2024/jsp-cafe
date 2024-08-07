package codesquad.article.service;

import codesquad.article.domain.Article;
import codesquad.article.repository.ArticleRepository;
import codesquad.comment.domain.Comment;
import codesquad.comment.domain.vo.Status;
import codesquad.comment.repository.CommentRepository;
import codesquad.common.exception.CommentExistException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;

import java.util.List;
import java.util.Optional;

public class DeleteArticleServiceImpl implements DeleteArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public DeleteArticleServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void delete(Command cmd) throws NoSuchElementException, UnauthorizedRequestException, CommentExistException {
        long articleId = cmd.articleId();
        String userId = cmd.userId();
        Optional<Article> findArticle = articleRepository.findByIdForUpdate(articleId);
        if (findArticle.isEmpty()) {
            throw new NoSuchElementException();
        }
        Article article = findArticle.get();
        article.delete(userId);
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        for (Comment comment : comments) {
            if (comment.getStatus() == Status.DELETED) {
                continue;
            }
            try {
                comment.delete(userId);
            } catch (UnauthorizedRequestException e) {
                throw new CommentExistException();
            }
        }
        articleRepository.update(article);
        commentRepository.updateStatus(comments, Status.DELETED);
    }
}
