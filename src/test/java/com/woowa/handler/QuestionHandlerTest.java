package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

import com.woowa.database.Page;
import com.woowa.database.question.QuestionDatabase;
import com.woowa.database.question.QuestionMemoryDatabase;
import com.woowa.database.user.UserDatabase;
import com.woowa.database.user.UserMemoryDatabase;
import com.woowa.exception.AuthorizationException;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.Author;
import com.woowa.model.Question;
import com.woowa.model.Reply;
import com.woowa.model.User;
import com.woowa.support.QuestionFixture;
import com.woowa.support.ReplyFixture;
import com.woowa.support.UserFixture;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionHandlerTest {

    private QuestionHandler questionHandler;
    private UserDatabase userDatabase;
    private QuestionDatabase questionDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        questionDatabase = new QuestionMemoryDatabase();
        questionHandler = new QuestionHandler(userDatabase, questionDatabase);
    }

    @Nested
    @DisplayName("createQuestion 호출 시")
    class CreateQuestionTest {

        @Test
        @DisplayName("질문을 생성한다.")
        void createQuestion() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            String title = "title";
            String content = "content";

            userDatabase.save(user);

            //when
            ResponseEntity response = questionHandler.createQuestion(user.getUserId(), title, content);

            //then
            List<Question> result = questionDatabase.findAll();
            assertThat(result).hasSize(1).first().satisfies(question -> {
                assertThat(question.getTitle()).isEqualTo(title);
                assertThat(question.getContent()).isEqualTo(content);
                assertThat(question.getAuthor()).isEqualTo(Author.from(user));
            });
        }

        @Test
        @DisplayName("메인 화면으로 리다이렉트한다.")
        void redirectToMain() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            String title = "title";
            String content = "content";

            userDatabase.save(user);

            //when
            ResponseEntity response = questionHandler.createQuestion(user.getUserId(), title, content);

            //then
            assertThat(response.getStatus()).isEqualTo(302);
            assertThat(response.getLocation()).isEqualTo("/");
        }

        @Test
        @DisplayName("예외(NoSuchElement): 존재하지 않는 사용자이면")
        void noSuchElement_whenUserNotFound() {
            //given
            String userId = "nope";
            String title = "title";
            String content = "content";

            //when
            Exception exception = catchException(() -> questionHandler.createQuestion(userId, title, content));

            //then
            assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("findQuestions 호출 시")
    class FindQuestionsTest {

        @Test
        @DisplayName("질문 목록이 생성된 시간 역순으로 반환된다.")
        void questionsOrderByCreatedAt() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            for (int i = 0; i < 10; i++) {
                Question question = Question.create(UUID.randomUUID().toString(), "title" + i, "content",
                        Author.from(user),
                        ZonedDateTime.now().plusHours(i));
                questionDatabase.save(question);
            }

            //when
            ResponseEntity response = questionHandler.findQuestions(0, 10);

            //then
            assertThat(response.getModel().get("questions")).isInstanceOf(Page.class);
            Page<Question> questions = (Page<Question>) response.getModel().get("questions");
            assertThat(questions.getContent()).isNotNull()
                    .asInstanceOf(LIST)
                    .hasSize(10)
                    .map(question -> ((Question) question).getCreatedAt())
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        @Test
        @DisplayName("페이지네이션이 적용된다.")
        void pagination() {
            //given
            User user = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            for (int i = 0; i < 20; i++) {
                Question question = Question.create(UUID.randomUUID().toString(), "title" + i, "content",
                        Author.from(user),
                        ZonedDateTime.now().plusHours(i));
                questionDatabase.save(question);
            }

            //when
            ResponseEntity response = questionHandler.findQuestions(1, 10);

            //then
            assertThat(response.getModel().get("questions")).isInstanceOf(Page.class);
            Page<Question> questions = (Page<Question>) response.getModel().get("questions");
            assertThat(questions.getContent()).isNotNull()
                    .asInstanceOf(LIST)
                    .hasSize(10)
                    .first()
                    .satisfies(question -> assertThat(((Question) question).getTitle()).isEqualTo("title" + 9));
        }
    }

    @Nested
    @DisplayName("findQuestion 호출 시")
    class FindQuestionTest {

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
        @DisplayName("단일 게시글을 조회한다.")
        void findQuestion() {
            //given

            //when
            ResponseEntity response = questionHandler.findQuestion(question.getQuestionId());

            //then
            Object result = response.getModel().get("question");
            assertThat(result).isNotNull().isInstanceOf(Question.class);
            Question findQuestion = (Question) result;
            assertThat(findQuestion.getTitle()).isEqualTo(question.getTitle());
            assertThat(findQuestion.getContent()).isEqualTo(question.getContent());
            assertThat(findQuestion.getAuthor()).isEqualTo(Author.from(user));
            assertThat(findQuestion.getCreatedAt()).isEqualTo(question.getCreatedAt());
        }

        @Test
        @DisplayName("댓글을 함께 조회한다.")
        void withReplies() {
            //given
            question.getReplies().addAll(List.of(
                    ReplyFixture.reply(user, question),
                    ReplyFixture.reply(user, question)
            ));

            //when
            ResponseEntity response = questionHandler.findQuestion(question.getQuestionId());

            //then
            Question findQuestion = (Question) response.getModel().get("question");
            assertThat(findQuestion.getReplies()).hasSize(2);
        }

        @Test
        @DisplayName("삭제 상태인 댓글은 조회하지 않는다.")
        void withoutDeletedReplies() {
            //given
            Reply deletedReply = ReplyFixture.reply(user, question);
            deletedReply.delete();
            question.getReplies().addAll(List.of(
                    ReplyFixture.reply(user, question),
                    ReplyFixture.reply(user, question),
                    deletedReply
            ));

            //when
            ResponseEntity response = questionHandler.findQuestion(question.getQuestionId());

            //then
            Question findQuestion = (Question) response.getModel().get("question");
            assertThat(findQuestion.getReplies()).hasSize(2)
                    .doesNotContain(deletedReply);
        }
    }

    @Nested
    @DisplayName("updateQuestionForm 호출 시")
    class UpdateQuestionFormTest {

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
        @DisplayName("예외(Authority): 유효한 사용자가 아니면")
        void checkUserAuthority() {
            //given
            User userB = UserFixture.user();
            userDatabase.save(userB);

            //when
            Exception exception = catchException(
                    () -> questionHandler.updateQuestionForm(userB.getUserId(), question.getQuestionId()));

            //then
            assertThat(exception).isInstanceOf(AuthorizationException.class);
        }

        @Test
        @DisplayName("질문 데이터를 모델에 추가한다.")
        void addQuestionToModel() {
            //given

            //when
            ResponseEntity response = questionHandler.updateQuestionForm(user.getUserId(), question.getQuestionId());

            //then
            assertThat(response.getModel().get("question")).isNotNull();
        }

        @Test
        @DisplayName("질문 수정 폼의 뷰 이름을 반환한다.")
        void returnViewName() {
            //given

            //when
            ResponseEntity response = questionHandler.updateQuestionForm(user.getUserId(), question.getQuestionId());

            //then
            assertThat(response.getViewName()).isEqualTo("/qna/update");
        }
    }

    @Nested
    @DisplayName("updateQuestion 호출 시")
    class UpdateQuestionTest {

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
        @DisplayName("질문을 업데이트한다.")
        void updateQuestion() {
            //given
            String title = "updateTitle";
            String content = "updateContent";

            //when
            questionHandler.updateQuestion(user.getUserId(), question.getQuestionId(), title, content);

            //then
            Question findQuestion = questionDatabase.findById(question.getQuestionId()).get();
            assertThat(findQuestion.getTitle()).isEqualTo(title);
            assertThat(findQuestion.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외(Authorization): 질문 작성자가 아니면")
        void authorization_WHenNoAuthor() {
            //given
            User anotherUser = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            userDatabase.save(anotherUser);

            //when
            Exception exception = catchException(() ->
                    questionHandler.updateQuestion(anotherUser.getUserId(), question.getQuestionId(), "update",
                            "update"));

            //then
            assertThat(exception).isInstanceOf(AuthorizationException.class);
        }

        @Test
        @DisplayName("질문 상세 조회 화면으로 리다이렉트한다.")
        void redirectToQuestion() {
            //given
            String title = "updateTitle";
            String content = "updateContent";

            //when
            ResponseEntity response = questionHandler.updateQuestion(user.getUserId(), question.getQuestionId(), title,
                    content);

            //then
            assertThat(response.getLocation()).isEqualTo("/questions/" + question.getQuestionId());
        }
    }

    @Nested
    @DisplayName("deleteQuestion 호출 시")
    class DeleteQuestionTest {

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
        @DisplayName("질문을 지운다.")
        void deleteQuestion() {
            //given

            //when
            questionHandler.deleteQuestion(user.getUserId(), question.getQuestionId());

            //then
            assertThat(questionDatabase.findById(question.getQuestionId())).isEmpty();
        }

        @Test
        @DisplayName("예외(Authorization): 작성자가 아니면")
        void authorization_WhenNoAuthor() {
            //given
            User anotherUser = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            userDatabase.save(anotherUser);

            //when
            Exception exception = catchException(
                    () -> questionHandler.deleteQuestion(anotherUser.getUserId(), question.getQuestionId()));

            //then
            assertThat(exception).isInstanceOf(AuthorizationException.class);
        }

        @Test
        @DisplayName("예외(Authorization): 다른 사용자의 댓글이 포함되어 있으면")
        void authorization_ContainsOtherUserReplies() {
            //given
            User anotherUser = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            question.getReplies().addAll(List.of(
                    ReplyFixture.reply(user, question),
                    ReplyFixture.reply(anotherUser, question)
            ));

            //when
            Exception exception = catchException(
                    () -> questionHandler.deleteQuestion(user.getUserId(), question.getQuestionId()));

            //then
            assertThat(exception).isInstanceOf(AuthorizationException.class);
        }

        @Test
        @DisplayName("다른 사용자의 댓글이 포함되어 있지 않으면 삭제한다.")
        void deleteQuestion_WhenNotContainsOtherUserReplies() {
            //given
            question.getReplies().addAll(List.of(
                    ReplyFixture.reply(user, question),
                    ReplyFixture.reply(user, question)
            ));

            //when
            ResponseEntity response = questionHandler.deleteQuestion(user.getUserId(), question.getQuestionId());

            //then
            assertThat(question.isDeleted()).isTrue();
            assertThat(question.getReplies()).allSatisfy(reply -> {
                assertThat(reply.isDeleted()).isTrue();
            });
        }

        @Test
        @DisplayName("다른 사용자의 삭제된 댓글은 무시한다.")
        void deletedQuestion_WhenContainsDeletedReply() {
            //given
            User anotherUser = User.create(UUID.randomUUID().toString(), "test@test.com", "password", "nickname");
            Reply reply = ReplyFixture.reply(anotherUser, question);
            reply.delete();
            question.getReplies().add(reply);

            //when
            ResponseEntity response = questionHandler.deleteQuestion(user.getUserId(), question.getQuestionId());

            //then
            assertThat(question.isDeleted()).isTrue();
        }
    }
}
