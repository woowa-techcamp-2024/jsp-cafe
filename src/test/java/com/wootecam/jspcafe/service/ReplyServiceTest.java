package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.BadRequestException;
import com.wootecam.jspcafe.exception.NotFoundException;
import com.wootecam.jspcafe.service.fixture.ServiceTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReplyServiceTest extends ServiceTest {

    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        replyService = new ReplyService(replyRepository);
    }

    @Nested
    class append_메소드는 {

        @Nested
        class 댓글입력이_정상적으로_인자로_전달되면 {

            @Test
            void 저장된_댓글을_반환한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));

                // when
                Reply reply = replyService.append(1L, 1L, "userId", "reply");

                // then
                assertAll(
                        () -> assertThat(reply.getWriter()).isEqualTo("userId"),
                        () -> assertThat(reply.getContents()).isEqualTo("reply"),
                        () -> assertThat(reply.getUserPrimaryId()).isEqualTo(1L),
                        () -> assertThat(reply.getQuestionPrimaryId()).isEqualTo(1L)
                );
            }
        }
    }

    @Nested
    class countAll_메소드는 {

        @Nested
        class 만약_특정_질문의_댓글이_없다면 {

            @Test
            void zero를_반환한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));

                // when
                int count = replyService.countAll(1L);

                // then
                assertThat(count).isZero();
            }
        }

        @Nested
        class 만약_특정_질문에_댓글이_N개라면 {

            @Test
            void N개를_반환한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("name", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("name", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("name", "contents", LocalDateTime.now(), 1L, 1L));

                // when
                int count = replyService.countAll(1L);

                // then
                assertThat(count).isEqualTo(3);
            }
        }
    }

    @Nested
    class readAll_메소드는 {

        @Nested
        class 질문에_댓글이_달려있다면 {

            @Test
            void 지정된_갯수의_댓글을_조회한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // when
                List<Reply> replies = replyService.readAll(1L, 2);

                // then
                assertThat(replies).hasSize(2);
            }
        }

        @Nested
        class 질문에_댓글이_없다면 {

            @Test
            void 댓글을_조회_결과가_비어있다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));

                // when
                List<Reply> replies = replyService.readAll(1L, 1);

                // then
                assertThat(replies).isEmpty();
            }
        }

        @Nested
        class 댓글_조회_갯수가_1개_미만이라면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));

                // expect
                assertThatThrownBy(() -> replyService.readAll(1L, 0))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("댓글 조회 갯수는 1개 이상이어야 합니다.");
            }
        }
    }

    @Nested
    class delete_메소드는 {

        @Nested
        class 질문에_존재하는_댓글이_있다면 {

            @Test
            void 댓글을_삭제한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // when
                replyService.delete(1L, 1L);
                List<Reply> replies = replyRepository.findAllByQuestionPrimaryIdLimit(1L, 1);

                // then
                assertThat(replies).isEmpty();
            }
        }

        @Nested
        class 질문에_존재하는_댓글을_작성한_사용자와_인자로_넘긴_사용자의_id가_다르다면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // expect
                assertThatThrownBy(() -> replyService.delete(1L, 2L))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("자신이 작성한 댓글만 삭제할 수 있습니다.");
            }
        }

        @Nested
        class 존재하지_않는_댓글을_삭제하려고_하면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // expect
                assertThatThrownBy(() -> replyService.delete(2L, 1L))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessage("댓글을 찾을 수 없습니다.");
            }
        }
    }

    @Nested
    class readAllStartsWith_메소드는 {

        @Nested
        class 조회할_댓글의_갯수가_1개_미만이라면 {

            @Test
            void 예외가_발생한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // expect
                assertThatThrownBy(() -> replyService.readAllStartsWith(1L, 1L, 0))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("댓글 조회 갯수는 1개 이상이어야 합니다.");
            }
        }

        @Nested
        class questionId와_마지막_조회_댓글id와_앞으로_조회할_댓글수를_전달하면 {

            @Test
            void 마지막_댓글_이후부터_댓글수만큼_조회합니다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("name", "contents1", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("name", "contents2", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("name", "contents3", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("name", "contents4", LocalDateTime.now(), 1L, 1L));

                // when
                List<Reply> replies = replyService.readAllStartsWith(1L, 2L, 2);

                assertThat(replies).extracting(Reply::getContents)
                        .containsExactly("contents3", "contents4");
            }
        }
    }
}
