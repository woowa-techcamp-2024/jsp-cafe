package woopaca.jspcafe.filter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMatcherTest {

    @Nested
    class matches_메서드는 {

        @Test
        void 메서드와_경로가_일치하는_경우_true를_반환한다() {
            RequestMatcher requestMatcher = new RequestMatcher("GET", "/test");
            assertThat(requestMatcher.matches("GET", "/test")).isTrue();
        }

        @Test
        void 메서드가_다른_경우_false를_반환한다() {
            RequestMatcher requestMatcher = new RequestMatcher("GET", "/test");
            assertThat(requestMatcher.matches("POST", "/test")).isFalse();
        }

        @Test
        void 경로가_다른_경우_false를_반환한다() {
            RequestMatcher requestMatcher = new RequestMatcher("GET", "/test");
            assertThat(requestMatcher.matches("GET", "/invalid")).isFalse();
        }

        @Test
        void 경로에_와일드카드가_포함된_경우_어떤_값이든_true를_반환한다() {
            RequestMatcher requestMatcher = new RequestMatcher("GET", "/test/*");
            assertThat(requestMatcher.matches("GET", "/test/1")).isTrue();
            assertThat(requestMatcher.matches("GET", "/test/zz")).isTrue();
        }
    }
}