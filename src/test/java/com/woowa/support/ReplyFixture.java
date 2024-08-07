package com.woowa.support;

import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.QuestionInfo;
import com.woowa.model.Reply;
import com.woowa.model.User;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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

    public static List<Reply> replies(User user, Question question, int size) {
        return IntStream.range(0, size).mapToObj(i -> reply(user, question)).toList();
    }
}
