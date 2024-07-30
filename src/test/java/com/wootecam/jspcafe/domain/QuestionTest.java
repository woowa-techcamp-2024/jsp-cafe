package com.wootecam.jspcafe.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void 질문_폼_입력에서_하나라도_비어있다면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new Question("", "안녕하세요!", "반갑습니다.", LocalDateTime.now(), 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
    }

    @Test
    void 질문_폼_입력에서_하나라도_null이_입력되면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new Question("HiiWee", "안녕하세요!", null, LocalDateTime.now(), 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
    }

    @Nested
    class isSameWriter_메소드는 {

        @Test
        void 동일한_사용자의_id가_인자로오면_true를_반환한다() {
            // given
            Question question = new Question(1L, "작성자", "제목", "내용", LocalDateTime.now(), 1L);

            // expect
            assertThat(question.isSameWriter(1L)).isTrue();
        }

        @Test
        void 다른_사용자의_id가_인자로오면_false를_반환한다() {
            // given
            Question question = new Question(1L, "작성자", "제목", "내용", LocalDateTime.now(), 1L);

            // expect
            assertThat(question.isSameWriter(2L)).isFalse();
        }
    }
}
