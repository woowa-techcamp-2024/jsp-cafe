package com.woowa.handler;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.framework.web.HttpMethod;
import com.woowa.framework.web.RequestMapping;
import com.woowa.framework.web.RequestParameter;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.User;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionHandler {

    private static final Logger log = LoggerFactory.getLogger(QuestionHandler.class);
    private final UserDatabase userDatabase;
    private final QuestionDatabase questionDatabase;

    public QuestionHandler(UserDatabase userDatabase, QuestionDatabase questionDatabase) {
        this.userDatabase = userDatabase;
        this.questionDatabase = questionDatabase;
    }

    @RequestMapping(path = "/questions", method = HttpMethod.POST)
    public ResponseEntity createQuestion(
            String userId,
            @RequestParameter("title") String title,
            @RequestParameter("content") String content) {
        User user = userDatabase.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        Question question = Question.create(UUID.randomUUID().toString(), title, content, Author.from(user),
                ZonedDateTime.now());
        log.info("새로운 질문이 등록되었습니다. qusetionId={}", question.getQuestionId());
        questionDatabase.save(question);
        return ResponseEntity.builder()
                .found("/");
    }
}
