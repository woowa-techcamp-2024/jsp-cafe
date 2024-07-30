package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.QuestionMemoryDatabase;
import com.woowa.database.ReplyDatabase;
import com.woowa.database.ReplyMemoryDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.model.Question;
import com.woowa.model.Reply;
import com.woowa.model.User;
import com.woowa.support.QuestionFixture;
import com.woowa.support.UserFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReplyHandlerTest {

    private ReplyHandler replyHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;
    private ReplyDatabase replyDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        questionDatabase = new QuestionMemoryDatabase();
        replyDatabase = new ReplyMemoryDatabase();
        replyHandler = new ReplyHandler(userDatabase, questionDatabase, replyDatabase);
    }

    @Nested
    @DisplayName("createReply 호출 시")
    class CreateReplyTest {

        private User user;
        private Question question;

        @BeforeEach
        void setUp() {
            user = UserFixture.user();
            question = QuestionFixture.question(user);
            userDatabase.save(user);
            questionDatabase.save(question);
        }

        @Test
        @DisplayName("댓글을 생성한다.")
        void createReply() {
            //given
            String content = "content";

            //when
            replyHandler.createReply(user.getUserId(), question.getQuestionId(), content);

            //then
            List<Reply> result = replyDatabase.findAll();
            assertThat(result).hasSize(1)
                    .first()
                    .satisfies(reply -> {
                        assertThat(reply.getContent()).isEqualTo(content);
                    });
        }
    }
}