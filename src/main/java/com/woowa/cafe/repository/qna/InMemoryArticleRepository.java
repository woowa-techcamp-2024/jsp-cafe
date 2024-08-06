package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.dto.article.ArticleQueryDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository {

    public AtomicLong articleId = new AtomicLong(1);
    private final Map<Long, Article> questions = new ConcurrentHashMap<>();

    @Override
    public Long save(final Article question) {
        question.setId(articleId.getAndIncrement());

        questions.put(question.getId(), question);

        return question.getId();
    }

    @Override
    public Optional<Article> findById(final Long articleId) {
        return Optional.ofNullable(questions.get(articleId));
    }

    @Override
    public List<ArticleQueryDto> findByPage(final int page, final int size) {
        return questions.values()
                .stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(article -> new ArticleQueryDto(article.getId(), article.getTitle(),
                        article.getFormattedUpdatedAt(), article.getWriterId(),
                        article.getReplyCount()))
                .toList();
    }

    @Override
    public List<Article> findAll() {
        return questions.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Article> update(final Article question) {
        return Optional.ofNullable(questions.replace(question.getId(), question));
    }

    @Override
    public void delete(final Long questionId) {
        questions.remove(questionId);
    }

    @Override
    public int countByPage() {
        return questions.size();
    }
}
