package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.fixture.ServiceTest;
import java.time.LocalDateTime;
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
}
