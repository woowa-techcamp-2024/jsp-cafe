package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
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
    class readAll_메소드는 {

        @Nested
        class 질문에_댓글이_달려있다면 {

            @Test
            void 모든_댓글을_조회한다() {
                // given
                userRepository.save(new User("userId", "password", "name", "mail@mail.com"));
                questionRepository.save(new Question("userId", "title", "contents", LocalDateTime.now(), 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));
                replyRepository.save(new Reply("userId", "contents", LocalDateTime.now(), 1L, 1L));

                // when
                List<Reply> replies = replyService.readAll(1L);

                // then
                assertThat(replies).hasSize(3);
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
                List<Reply> replies = replyService.readAll(1L);

                // then
                assertThat(replies).isEmpty();
            }
        }
    }
}
