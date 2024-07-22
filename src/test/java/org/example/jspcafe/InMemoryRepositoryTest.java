package org.example.jspcafe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryRepositoryTest {

    @DisplayName("PK가 없는 엔티티를 저장하면 예외가 발생한다")
    @Test
    void saveNoPKEntity() {
        // when & then
        assertThatThrownBy(() -> new NoPKEntityRepository())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity 클래스에 @PK를 찾을 수 없습니다." + NoPKEntity.class.getName());

    }


    static class NoPKEntity {
        private Long id;
        private String name;

        public NoPKEntity(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static class NoPKEntityRepository extends InMemoryRepository<NoPKEntity> {
        protected NoPKEntityRepository() {
            super(NoPKEntity.class);
        }
    }



}