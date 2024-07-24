package com.woowa.framework.argumentresovler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.woowa.framework.web.HttpMethod;
import com.woowa.framework.web.RequestMapping;
import com.woowa.framework.web.RequestParameter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RequestParameterResolverTest {

    private RequestParameterResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new RequestParameterResolver();
    }

    private static class TestHandler {

        public String hello(@RequestParameter("content") String content) {
            return content;
        }

        public String notSupport(String content) {
            return content;
        }
    }

    @Nested
    @DisplayName("supportsParameter 호출 시")
    class SupportsParameter {

        @Test
        @DisplayName("파라미터 형식을 지원하면 true를 반환한다.")
        void supportsParameter() throws NoSuchMethodException {
            //given
            Method method = TestHandler.class.getMethod("hello", String.class);
            Parameter[] parameters = method.getParameters();
            Parameter parameter = parameters[0];

            //when
            boolean isSupports = resolver.supportsParameter(parameter);

            //then
            assertThat(isSupports).isTrue();
        }

        @Test
        @DisplayName("파라미터 형식을 지원하지 않으면 false를 반환한다.")
        void test() throws NoSuchMethodException {
            //given
            Method method = TestHandler.class.getMethod("notSupport", String.class);
            Parameter[] parameters = method.getParameters();
            Parameter parameter = parameters[0];

            //when
            boolean isSupports = resolver.supportsParameter(parameter);

            //then
            assertThat(isSupports).isFalse();
        }
    }
}
