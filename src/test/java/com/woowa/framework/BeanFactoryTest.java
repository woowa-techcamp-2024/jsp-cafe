package com.woowa.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.woowa.config.HandlerConfig;
import com.woowa.database.user.UserDatabase;
import com.woowa.framework.argumentresovler.ArgumentResolverComposite;
import com.woowa.handler.UserHandler;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BeanFactoryTest {

    private BeanFactory container;

    @BeforeEach
    void setUp() {
        container = new BeanFactory();
    }

    @Nested
    @DisplayName("start 호출 시")
    class StartTest {

        @Test
        @DisplayName("기본 구성의 ArgumentResovler가 등록된다.")
        void test() {
            //given

            //when
            container.start();

            //then
            ArgumentResolverComposite argumentResolverComposite = container.getBean(ArgumentResolverComposite.class);
            assertThat(argumentResolverComposite).isNotNull();
        }
    }

    @Nested
    @DisplayName("getBean 호출 시")
    class GetBeanTest {

        @BeforeEach
        void setUp() {
            container.start();
        }

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
            assertThat(bean).isNotNull();
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

    @Nested
    @DisplayName("getBeans 호출 시")
    class GetBeansTest {


        @BeforeEach
        void setUp() {
            container.start();
        }

        @Test
        @DisplayName("인터페이스의 구현체를 조회할 수 있다.")
        void findImplementation() {
            //given

            //when
            List<Initializer> beans = container.getBeans(Initializer.class);

            //then
            assertThat(beans).isNotEmpty();
        }
    }
}