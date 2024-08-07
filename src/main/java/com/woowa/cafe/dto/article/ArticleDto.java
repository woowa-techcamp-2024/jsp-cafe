package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;
import com.woowa.cafe.domain.Reply;

import java.util.List;

public record ArticleDto(Long articleId, String title, String contents, String updatedAt, String writerId,
                         String writerName, Long replyCount, List<ReplyDto> replies) {
    public static ArticleDto of(final Article article, final Member member, final List<Reply> replies, final List<Member> members) {
        return new ArticleDto(article.getId(), article.getTitle(), article.getContents(),
                article.getFormattedUpdatedAt(), member.getMemberId(), member.getName(), article.getReplyCount(), ReplyDto.mapToList(replies, members));
    }
}
