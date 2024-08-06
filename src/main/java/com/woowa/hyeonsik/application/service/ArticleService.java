package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.dao.ArticleDao;
import com.woowa.hyeonsik.application.dao.CommentDao;
import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.domain.Page;
import com.woowa.hyeonsik.application.domain.Reply;
import com.woowa.hyeonsik.application.exception.AuthorizationException;

import java.util.List;
import java.util.Optional;

public class ArticleService {
    private final ArticleDao articleDao;
    private final CommentDao commentDao;

    public ArticleService(ArticleDao articleDao, final CommentDao commentDao) {
        this.articleDao = articleDao;
        this.commentDao = commentDao;
    }

    public void write(Article article) {
        articleDao.save(article);
    }

    public Page<Article> list() {
        return list(1);
    }

    public Page<Article> list(long page) {
        return articleDao.findAll(page);
    }

    public Article findById(long articleId) {
        return articleDao.findByArticleId(articleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void update(Article article, String userId) {
        Optional<Article> foundArticle = articleDao.findByArticleId(article.getId());
        if (!foundArticle.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        if (!foundArticle.get().getWriter().equals(userId)) {
            throw new AuthorizationException("유저 정보가 일치하지 않습니다.");
        }

        articleDao.update(article);
    }

    public void remove(long articleId, String userId) {
        Optional<Article> foundArticle = articleDao.findByArticleId(articleId);
        if (!foundArticle.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        if (!foundArticle.get().getWriter().equals(userId)) {
            throw new AuthorizationException("유저 정보가 일치하지 않습니다.");
        }

        validateComments(articleId, userId);
        articleDao.removeByArticleId(articleId);
        commentDao.removeByArticleId(articleId);  // 해당 게시글의 댓글돌 모두 삭제
    }

    private void validateComments(final long articleId, final String userId) {
        final List<Reply> comments = commentDao.findAllByArticleId(articleId);

        // 다른 사람의 댓글이 잇는지 확인
        final long count = comments.stream()
            .filter(comment -> !comment.getWriter().equals(userId))
            .count();
        if (count == 0) {
            return;
        }
        throw new IllegalStateException("다른 사람의 댓글이 있는 경우 삭제할 수 없습니다.");
    }
}
