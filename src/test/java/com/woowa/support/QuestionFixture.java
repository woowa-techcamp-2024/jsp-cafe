package com.woowa.support;

import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.User;
import java.time.ZonedDateTime;
import java.util.UUID;

public class QuestionFixture {
    public static Question question(User user) {
        return Question.create(UUID.randomUUID().toString(), "title", "content", Author.from(user),
                ZonedDateTime.now());
    }
}
