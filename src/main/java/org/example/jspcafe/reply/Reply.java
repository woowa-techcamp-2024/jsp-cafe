package org.example.jspcafe.reply;

import lombok.Builder;
import lombok.Getter;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.user.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class Reply {
    Long id;
    Long userId;
    User user;
    Long questionId;
    Question question;
    String contents;
    LocalDateTime lastModifiedDate;
}
