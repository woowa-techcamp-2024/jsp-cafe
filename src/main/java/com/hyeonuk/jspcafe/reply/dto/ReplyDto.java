package com.hyeonuk.jspcafe.reply.dto;

import com.hyeonuk.jspcafe.reply.domain.Reply;

public class ReplyDto {
    private Long replyId;
    private Long memberId;
    private String memberNickname;
    private Long articleId;
    private String contents;

    public ReplyDto(){}

    public ReplyDto(Long replyId, Long memberId,String memberNickname, Long articleId, String contents) {
        this.replyId = replyId;
        this.memberId = memberId;
        this.articleId = articleId;
        this.contents = contents;
        this.memberNickname = memberNickname;
    }

    public static ReplyDto from(Reply reply) {
        return new ReplyDto(reply.getId(),reply.getMember().getId(),reply.getMember().getNickname(),reply.getArticle().getId(),reply.getContents());
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }
}
