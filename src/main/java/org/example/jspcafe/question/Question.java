package org.example.jspcafe.question;

import lombok.Builder;
import org.example.jspcafe.user.User;

import javax.sql.RowSet;
import java.time.LocalDateTime;

public class Question {
    private Long id;
    private Long userId;
    private User user;
    private String title;
    private String contents;
    private LocalDateTime lastModifiedDate;


    @Builder
    private Question(Long id, Long userId, User user, String title, String contents, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }
}
