package com.woowa.support;

import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.QuestionInfo;
import com.woowa.model.Reply;
import com.woowa.model.User;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ReplyFixture {
    public static Reply reply(User user, Question question) {
        return Reply.create(
                UUID.randomUUID().toString(),
                "content",
                Author.from(user),
                QuestionInfo.from(question),
                ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        );
    }
}
