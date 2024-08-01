package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Reply;
import com.woowa.cafe.dto.article.SaveReplyDto;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.ReplyRepository;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ReplyService {

    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    public ReplyService(final ArticleRepository articleRepository, final ReplyRepository replyRepository) {
        this.articleRepository = articleRepository;
        this.replyRepository = replyRepository;
    }

    public Long save(final SaveReplyDto replyDto, final String writerId) {
        Article article = articleRepository.findById(replyDto.articleId())
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        Reply reply = replyDto.toEntity(writerId);
        replyRepository.save(reply);

        article.increaseReplyCount();
        articleRepository.update(article);

        return reply.getArticleId();
    }

    public void delete(final Long replyId, final String memberId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 댓글입니다."));

        Article article = articleRepository.findById(reply.getArticleId())
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        if (!reply.isSameWriter(memberId)) {
            throw new HttpException(SC_FORBIDDEN, "다른 사람이 삭제할 수 없습니다.");
        }

        article.decreaseReplyCount();
        articleRepository.update(article);

        replyRepository.delete(replyId);
    }


}
