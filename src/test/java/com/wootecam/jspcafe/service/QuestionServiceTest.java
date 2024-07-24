package com.wootecam.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wootecam.jspcafe.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QuestionServiceTest {

    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionRepository = new QuestionRepository();
        questionService = new QuestionService(questionRepository);
    }

    @Nested
    class append_메소드는 {

        @Nested
        class 만약_질문_정보가_하나라도_비어있다면 {

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> questionService.append("", "제목입니다.", "내용입니다."))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
            }
        }

        @Nested
        class 만약_질문_정보에_하나라도_null이_포함되면 {

            @Test
            void 예외를_발생시킨다() {
                assertThatThrownBy(() -> questionService.append(null, "제목입니다.", "내용입니다."))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("질문 작성 시 모든 정보를 입력해야 합니다.");
            }
        }

        @Nested
        class 정상적인_질문_입력이라면 {

            @Test
            void 질문을_저장합니다() {
                // expect
                assertThatNoException()
                        .isThrownBy(() -> questionService.append("작성자", "제목입니다.", "내용입니다."));
            }
        }
    }
}
