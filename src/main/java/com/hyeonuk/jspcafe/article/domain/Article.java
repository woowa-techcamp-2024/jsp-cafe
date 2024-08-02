package com.hyeonuk.jspcafe.article.domain;

import com.hyeonuk.jspcafe.member.domain.Member;

import java.util.Date;

public class Article {
    private Long id;
    private Member writer;
    private String title;
    private String contents;
    private Date deletedAt;

    public Article(Long id,Member writer,String title,String contents){
        this.id=id;
        this.writer=writer;
        this.title = title;
        this.contents = contents;
    }
    public Article(Member writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }
    public Article(Long id, Member writer, String title, String contents, Date deletedAt) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getWriter() {
        return writer;
    }

    public void setWriter(Member writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean validation(){
        return this.title != null && !this.title.isBlank()
                && this.contents!=null && !this.contents.isBlank()
                && this.writer != null && this.writer.getId() != null;
    }
}
