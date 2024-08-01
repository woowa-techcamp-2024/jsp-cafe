package woopaca.jspcafe.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMatchersTest {

    Set<RequestMatcher> includeMatchers;
    RequestMatchers requestMatchers;

    @BeforeEach
    void setUp() {
        includeMatchers = new HashSet<>();
        requestMatchers = new RequestMatchers(includeMatchers);
    }

    @Nested
    class addMatcher_메서드는 {

        @Test
        void 새로운_매처를_추가한다() {
            requestMatchers.addMatcher("GET", "/test");
            requestMatchers.addMatcher("POST", "/test");
            requestMatchers.addMatcher("GET", "/test/*");
            assertThat(includeMatchers).hasSize(3);
        }

        @Test
        void 중복된_매처를_추가해도_중복되어_저장되지_않는다() {
            requestMatchers.addMatcher("GET", "/test");
            requestMatchers.addMatcher("GET", "/test");
            assertThat(includeMatchers).hasSize(1);
        }
    }

    @Nested
    class contains_메서드는 {

        @Test
        void 매처에_일치하는_경로가_있으면_true를_반환한다() {
            includeMatchers.add(new RequestMatcher("GET", "/test"));
            includeMatchers.add(new RequestMatcher("POST", "/test"));
            includeMatchers.add(new RequestMatcher("GET", "/test/*"));

            assertThat(requestMatchers.contains("GET", "/test")).isTrue();
            assertThat(requestMatchers.contains("POST", "/test")).isTrue();
            assertThat(requestMatchers.contains("GET", "/test/1")).isTrue();
            assertThat(requestMatchers.contains("GET", "/test/zz")).isTrue();
        }

        @Test
        void 매처에_일치하는_경로가_없으면_false를_반환한다() {
            includeMatchers.add(new RequestMatcher("GET", "/test"));
            includeMatchers.add(new RequestMatcher("POST", "/test"));
            includeMatchers.add(new RequestMatcher("GET", "/test/*"));

            assertThat(requestMatchers.contains("GET", "/invalid")).isFalse();
            assertThat(requestMatchers.contains("POST", "/invalid")).isFalse();
            assertThat(requestMatchers.contains("GET", "/invalid/1")).isFalse();
            assertThat(requestMatchers.contains("GET", "/invalid/zz")).isFalse();
        }
    }
}