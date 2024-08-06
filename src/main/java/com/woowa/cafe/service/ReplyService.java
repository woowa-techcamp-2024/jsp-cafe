package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.domain.Reply;
import com.woowa.cafe.dto.article.ReplyDto;
import com.woowa.cafe.dto.article.SaveReplyDto;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.ReplyRepository;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ReplyService {

    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;

    public ReplyService(final ArticleRepository articleRepository, final ReplyRepository replyRepository, final MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.replyRepository = replyRepository;
        this.memberRepository = memberRepository;
    }

    public ReplyDto save(final SaveReplyDto replyDto, final String writerId) {
        Article article = articleRepository.findById(replyDto.articleId())
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        Reply reply = replyDto.toEntity(writerId);
        replyRepository.save(reply);

        article.increaseReplyCount();
        articleRepository.update(article);

        return ReplyDto.of(reply, writerId);
    }

    public List<ReplyDto> findByArticleIdWithPage(final Long articleId, final int index, final int size) {
        List<Reply> replies = replyRepository.findByPage(articleId, index, size);
        List<Member> members = memberRepository.findMembersByIds(replies.stream()
                .map(Reply::getWriterId)
                .toList());

        return ReplyDto.mapToList(replies, members);
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
