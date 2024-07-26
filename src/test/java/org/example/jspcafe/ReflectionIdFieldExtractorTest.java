package org.example.jspcafe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionIdFieldExtractorTest {

    static class TestEntity {
        private Long id;
        private String name;
    }

    @DisplayName("PK 어노테이션이 없는 경우 예외를 던진다")
    @Test
    void findIdFieldWithoutPKAnnotation() {
        // given
        Class<TestEntity> clazz = TestEntity.class;

        // when & then
        assertThatThrownBy(() -> new ReflectionIdFieldExtractor<>(clazz))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity 클래스에 @PK를 찾을 수 없습니다: " + clazz.getName());
    }

}