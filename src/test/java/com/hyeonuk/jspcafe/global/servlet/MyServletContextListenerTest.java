package com.hyeonuk.jspcafe.global.servlet;

import com.hyeonuk.jspcafe.member.servlets.mock.MockServletContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("MyServletContextListener 클래스")
class MyServletContextListenerTest {
    private ServletContext servletContext;
    private ServletContextEvent servletContextEvent;
    private MyServletContextListener myServletContextListener;

    @BeforeEach
    void setUp() {
        servletContext = new MockServletContext();
        servletContextEvent = new ServletContextEvent(servletContext);
        myServletContextListener = new MyServletContextListener();
    }

    @Nested
    @DisplayName("contextInitialized 메서드")
    class ContextInitialized {
        @DisplayName("메서드를 호출하면 필요한 인스턴스들이 전부 servletContext안에 setting된다.")
        @Test
        void contextInitialized() {
            //given

            //when
            myServletContextListener.contextInitialized(servletContextEvent);

            //then
            assertNotNull(servletContext.getAttribute("memberDao"));
            assertNotNull(servletContext.getAttribute("articleDao"));
            assertNotNull(servletContext.getAttribute("passwordEncoder"));
        }
    }
}