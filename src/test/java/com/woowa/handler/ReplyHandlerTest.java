package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.woowa.database.QuestionDatabase;
import com.woowa.database.QuestionMemoryDatabase;
import com.woowa.database.ReplyDatabase;
import com.woowa.database.ReplyMemoryDatabase;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.exception.AuthorizationException;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.Question;
import com.woowa.model.Reply;
import com.woowa.model.User;
import com.woowa.support.QuestionFixture;
import com.woowa.support.ReplyFixture;
import com.woowa.support.UserFixture;
import java.util.List;
import java.util.UUID;
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

    @Nested
    @DisplayName("deleteReply 호출 시")
    class DeleteReplyTest {

        private User user;
        private Question question;
        private Reply reply;

        @BeforeEach
        void setUp() {
            user = UserFixture.user();
            question = QuestionFixture.question(user);
            reply = ReplyFixture.reply(user, question);
            userDatabase.save(user);
            questionDatabase.save(question);
            replyDatabase.save(reply);
        }

        @Test
        @DisplayName("댓글을 삭제한다.")
        void deleteReply() {
            //given

            //when
            replyHandler.deleteReply(user.getUserId(), question.getQuestionId(), reply.getReplyId());

            //then
            assertThat(replyDatabase.findById(reply.getReplyId())).isEmpty();
        }

        @Test
        @DisplayName("게시글 상세 조회로 이동한다.")
        void locationIsQuestionDetail() {
            //given

            //when
            ResponseEntity response = replyHandler.deleteReply(user.getUserId(), question.getQuestionId(),
                    reply.getReplyId());

            //then
            assertThat(response.getLocation()).isEqualTo("/questions/" + question.getQuestionId());
        }

        @Test
        @DisplayName("예외(Authorization): 작성자가 아니면")
        void authorization_WhenNoAuthor() {
            //given
            User anotherUser = User.create(UUID.randomUUID().toString(), "test@test.com", "estsetset", "testset");
            userDatabase.save(anotherUser);

            //when
            Exception exception = catchException(
                    () -> replyHandler.deleteReply(anotherUser.getUserId(), question.getQuestionId(), reply.getReplyId()));

            //then
            assertThat(exception).isInstanceOf(AuthorizationException.class);
        }
    }
}