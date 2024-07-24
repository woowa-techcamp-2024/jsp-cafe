package com.wootecam.jspcafe.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void 질문_폼_입력에서_하나라도_비어있다면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new Question("", "안녕하세요!", "반갑습니다."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
    }

    @Test
    void 질문_폼_입력에서_하나라도_null이_입력되면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new Question("HiiWee", "안녕하세요!", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
    }
}
