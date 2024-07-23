package com.woowa.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.woowa.config.HandlerConfig;
import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.handler.UserHandler;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BeanFactoryTest {

    private BeanFactory container;

    @BeforeEach
    void setUp() {
        container = new BeanFactory();
        container.start();
    }

    @Nested
    @DisplayName("getBean 호출 시")
    class GetBeanTest {

        @Test
        @DisplayName("예외(NoSuchElement): 등록되지 않은 객체를 조회하면")
        void noSuchElement_WhenBeanNotFound() {
            //given

            //when
            Exception exception = catchException(() -> container.getBean(HandlerConfig.class));

            //then
            assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("인터페이스의 구현체를 조회할 수 있다.")
        void findImplementation() {
            //given

            //when
            UserDatabase bean = container.getBean(UserDatabase.class);

            //then
            assertThat(bean).isInstanceOf(UserMemoryDatabase.class);
        }

        @Test
        @DisplayName("등록된 객체를 조회할 수 있다.")
        void getBean() {
            //given

            //when
            UserHandler bean = container.getBean(UserHandler.class);

            //then
            assertThat(bean).isInstanceOf(UserHandler.class);
        }
    }
}