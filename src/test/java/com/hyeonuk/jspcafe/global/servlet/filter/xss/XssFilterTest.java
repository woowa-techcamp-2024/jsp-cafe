package com.hyeonuk.jspcafe.global.servlet.filter.xss;

import com.hyeonuk.jspcafe.global.servlet.filter.mock.MockFilterChain;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletRequest;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("XssFilter 클래스")
class XssFilterTest {
    private MockFilterChain filterChain;
    private MockRequest request;
    private MockResponse response;
    private Filter xssFilter;

    @BeforeEach
    void setUp() throws Exception {
        filterChain = new MockFilterChain();
        request = new MockRequest();
        response = new MockResponse();
        xssFilter = new XssFilter();
    }

    @Nested
    @DisplayName("doChain을 진행하면")
    class DoChain{
        @Test
        @DisplayName("request객체가 XssHttpServletRequestWrapper로 감싸진뒤 다음 chain을 실행한다..")
        void wrappingXssHttpServletRequestWrapper() throws ServletException, IOException {
            //given

            //when
            xssFilter.doFilter(request,response,filterChain);

            //then
            assertTrue(filterChain.getRequest() instanceof HttpServletRequestWrapper);
            assertEquals(1,filterChain.getCallCount());
        }
    }

    private class MockRequest extends BaseHttpServletRequest {

    }
    private class MockResponse extends BaseHttpServletResponse {

    }
}