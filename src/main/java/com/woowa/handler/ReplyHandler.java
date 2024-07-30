package com.woowa.handler;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.ReplyDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.framework.web.HttpMethod;
import com.woowa.framework.web.RequestMapping;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.QuestionInfo;
import com.woowa.model.Reply;
import com.woowa.model.User;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ReplyHandler {

    private final UserDatabase userDatabase;
    private final QuestionDatabase questionDatabase;
    private final ReplyDatabase replyDatabase;

    public ReplyHandler(UserDatabase userDatabase, QuestionDatabase questionDatabase, ReplyDatabase replyDatabase) {
        this.userDatabase = userDatabase;
        this.questionDatabase = questionDatabase;
        this.replyDatabase = replyDatabase;
    }

    @RequestMapping(path = "/questions/{questionId}/replies", method = HttpMethod.POST)
    public ResponseEntity createReply(String userId, String questionId, String content) {
        User user = userDatabase.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        Question question = questionDatabase.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 질문입니다."));
        Reply reply = Reply.create(
                UUID.randomUUID().toString(),
                content,
                Author.from(user),
                QuestionInfo.from(question),
                ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        );
        replyDatabase.save(reply);
        return ResponseEntity.builder()
                .found("/questions/" + questionId);
    }
}
