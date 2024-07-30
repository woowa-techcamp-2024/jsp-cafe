package com.hyeonuk.jspcafe.article.domain;

import com.hyeonuk.jspcafe.member.domain.Member;

public class Article {
    private Long id;
    private Member writer;
    private String title;
    private String contents;

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

    public boolean validation(){
        return this.title != null && !this.title.isBlank()
                && this.contents!=null && !this.contents.isBlank()
                && this.writer != null && this.writer.getId() != null;
    }
}
