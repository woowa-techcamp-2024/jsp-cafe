package codesquad.comment.service;

import codesquad.article.domain.Article;
import codesquad.article.repository.ArticleRepository;
import codesquad.comment.domain.Comment;
import codesquad.comment.domain.vo.Status;
import codesquad.comment.repository.CommentRepository;
import codesquad.common.exception.NoSuchElementException;

import java.util.Optional;

public class RegisterCommentServiceImpl implements RegisterCommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public RegisterCommentServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public Long register(RegisterCommentService.Command cmd) throws NoSuchElementException {
        Optional<Article> findArticle = articleRepository.findByIdForShare(cmd.articleId());
        if (findArticle.isEmpty()) {
            throw new NoSuchElementException();
        }
        return commentRepository.save(new Comment(cmd.articleId(), cmd.writer(), cmd.content(), Status.COMMENTED));
    }
}
