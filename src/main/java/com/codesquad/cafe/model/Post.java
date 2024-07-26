package com.codesquad.cafe.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String fileName;
    private int view;
    private LocalDateTime createdAt;

    public Post() {
    }

    public Post(Long userId, String title, String contents, String fileName) {
        this.id = null;
        this.userId = userId;
        this.title = title;
        this.content = contents;
        this.fileName = fileName;
        this.view = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Post(Long id, Long userId, String title, String content, String fileName, int view,
                LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.view = view;
        this.fileName = fileName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public int getView() {
        return view;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void addView() {
        this.view++;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", filename='").append(fileName).append('\'');
        sb.append(", view='").append(view).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
