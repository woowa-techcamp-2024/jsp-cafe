package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.domain.Reply;

import java.util.ArrayList;
import java.util.List;

public record ReplyDto(Long replyId, Long articleId, String writerId, String writerName, String contents,
                       String createAt, String updatedAt) {

    public static ReplyDto of(final Reply reply, final String writerName) {
        return new ReplyDto(reply.getId(), reply.getArticleId(), reply.getWriterId(), writerName, reply.getContents(),
                reply.getFormattedCreatedAt(), reply.getFormattedUpdatedAt());
    }

    public static List<ReplyDto> mapToList(final List<Reply> replies, final List<Member> members) {
        List<ReplyDto> replyDtos = new ArrayList<>();
        for (Reply reply : replies) {
            for (Member member : members) {
                if (reply.getWriterId().equals(member.getMemberId())) {
                    replyDtos.add(ReplyDto.of(reply, member.getName()));
                }
            }
        }

        return replyDtos;
    }
}
