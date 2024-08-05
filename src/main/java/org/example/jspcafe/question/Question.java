package org.example.jspcafe.question;

import lombok.Builder;
import lombok.Getter;
import org.example.jspcafe.user.User;

import javax.sql.RowSet;
import java.time.LocalDateTime;

@Getter
public class Question {
    private Long id;
    private Long userId;
    private User user;
    private String title;
    private String contents;
    private LocalDateTime lastModifiedDate;
    private boolean status;


    @Builder
    private Question(Long id, Long userId, User user, String title, String contents, LocalDateTime lastModifiedDate,boolean status) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.lastModifiedDate = lastModifiedDate;
        this.status = status;
    }

    public static void updateQuestion(Question question, String title, String contents) {
        question.title = title;
        question.contents = contents;
        question.lastModifiedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
