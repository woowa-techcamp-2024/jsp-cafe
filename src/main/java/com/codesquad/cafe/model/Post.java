package com.codesquad.cafe.model;

import java.time.LocalDateTime;

public class Post {

    private Long id;

    private Long authorId;

    private String title;

    private String content;

    private String fileName;

    private int view;

    private LocalDateTime createdAt;

    public Post() {
    }

    public Post(Long id, Long authorId, String title, String content, String fileName, int view,
                LocalDateTime createdAt) {
        if (authorId == null || authorId < 1) {
            throw new IllegalArgumentException("authorId는 1 이상의 값이어야 합니다.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title 필수 값입니다.");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("content 필수 값입니다.");
        }
        if (view < 0) {
            throw new IllegalArgumentException("view 는 0이상이어야 합니다.");
        }
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.view = view;
        this.fileName = fileName;
        this.createdAt = createdAt;
    }

    public static Post of(Long authorId, String title, String content, String fileName) {
        return new Post(null, authorId, title, content, fileName, 0, LocalDateTime.now());
    }


    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
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
        sb.append(", userId=").append(authorId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", filename='").append(fileName).append('\'');
        sb.append(", view='").append(view).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
