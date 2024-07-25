package org.example.jspcafe.question;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.jspcafe.common.DateTimeUtil.getCurrentDateTimeString;

public class Question {
    private Long id;
    private String writer;
    private String title;
    private String contents;
    private String date;
    public Question(String writer, String title, String contents) {
        this.date = getCurrentDateTimeString();
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
