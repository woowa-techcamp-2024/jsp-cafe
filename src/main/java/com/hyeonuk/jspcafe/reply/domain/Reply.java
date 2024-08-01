package com.hyeonuk.jspcafe.reply.domain;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.member.domain.Member;

public class Reply {
    private Long id;
    private Article article;
    private Member member;
    private String contents;

    public Reply(Long id, Article article, Member member, String contents) {
        this.id = id;
        this.article = article;
        this.member = member;
        this.contents = contents;
    }
    public Reply(Article article,Member member, String contents) {
        this(null,article,member,contents);
    }

    public boolean validation(){
        return this.id != null
                && this.article != null && this.article.getId() != null
                && this.member!= null && this.member.getId() != null
                && this.contents!=null && !this.contents.isBlank();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
