package com.woowa.cafe.service;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.domain.Reply;
import com.woowa.cafe.dto.article.*;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.ReplyRepository;
import com.woowa.cafe.utils.ArticleCountCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    public ArticleService(final ArticleRepository articleRepository, final MemberRepository memberRepository, final ReplyRepository replyRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.replyRepository = replyRepository;
    }

    public Long save(final SaveArticleDto saveArticleDto, String writerId) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 사용자입니다."));

        Long save = articleRepository.save(saveArticleDto.toEntity(member.getMemberId()));

        ArticleCountCache.increase();

        return save;
    }

    public List<ArticleListDto> findAll() {
        List<Article> articles = articleRepository.findAll();

        List<Member> members = memberRepository.findMembersByIds(articles.stream()
                .map(Article::getWriterId)
                .toList());

        return ArticleListDto.mapToList(articles, members);
    }

    public ArticlePageDto findByPage(final int page, final int size) {
        List<ArticleQueryDto> articles = articleRepository.findByPage(page, size);

        List<Member> members = memberRepository.findMembersByIds(articles.stream()
                .map(ArticleQueryDto::writerId)
                .toList());

        List<ArticleListDto> dtos = ArticleListDto.toDtos(articles, members);

        log.info("page: {}, size: {}", page, size);
        int count = ArticleCountCache.getCount();

        int pagesSize = size * 5 + 1;

        log.info("count: {}, pagesSize: {}", count, pagesSize);
        if (count >= pagesSize) {
            return ArticlePageDto.of(dtos, page, count, true);
        }

        return ArticlePageDto.of(dtos, page, count, false);
    }

    public ArticleDto findById(final Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        Member member = memberRepository.findById(article.getWriterId())
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 사용자입니다."));

        List<Reply> replies = replyRepository.findByPage(articleId, 1, 5);
        List<Member> members = memberRepository.findMembersByIds(replies.stream()
                .map(Reply::getWriterId)
                .toList());

        return ArticleDto.of(article, member, replies, members);
    }

    public void update(final Long articleId, final SaveArticleDto from, final String memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        if (!article.isSameWriter(memberId)) {
            throw new HttpException(SC_NOT_FOUND, "다른 사람이 수정할 수 없습니다.");
        }

        article.update(from.title(), from.content());
        articleRepository.update(article);
    }

    public void delete(final Long articleId, final String memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "존재하지 않는 게시글입니다."));

        if (!article.isSameWriter(memberId)) {
            throw new HttpException(SC_NOT_FOUND, "다른 사람이 삭제할 수 없습니다.");
        }

        List<Reply> replies = replyRepository.findByArticleId(articleId);

        long myReplyCnt = replies.stream()
                .filter(reply -> reply.isSameWriter(memberId))
                .count();

        if (replies.size() != myReplyCnt) {
            throw new HttpException(SC_NOT_FOUND, "다른 사람이 작성한 댓글이 있어 삭제할 수 없습니다.");
        }
        replyRepository.deleteByArticleId(articleId);

        articleRepository.delete(articleId);

        ArticleCountCache.decrease();
    }
}
