package codesqaud.app;

import codesqaud.mock.MockHttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestMapperTest {
    private RequestMapper requestMapper;
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Nested
    @DisplayName("RequestMapper와 HttpRequest를 대조할 때")
    class Matches {
        @Test
        @DisplayName("method와 url이 일치하면 true를 반환한다.")
        void given_equals_method_and_url() {
            // given
            requestMapper = RequestMapper.permittedGetMapping("/hello");
            request = new MockHttpServletRequest();
            request.setMethod("GET");
            request.setRequestURI("/hello");

            //when
            boolean matches = requestMapper.matches(request);

            //then
            assertThat(matches).isTrue();
        }

        @Test
        @DisplayName("method가 일치하지 않으면 false를 반환한다.")
        void given_different_url() {
            // given
            requestMapper = RequestMapper.permittedGetMapping("/hi");
            request = new MockHttpServletRequest();
            request.setMethod("POST");
            request.setRequestURI("/hello");

            //when
            boolean matches = requestMapper.matches(request);

            //then
            assertThat(matches).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"/hello", "/hi/hello"})
        @DisplayName("url이 일치하지 않으면 false를 반환한다.")
        void given_different_method(String requestURI) {
            // given
            requestMapper = RequestMapper.permittedGetMapping("/hi");
            request = new MockHttpServletRequest();
            request.setMethod("GET");
            request.setRequestURI(requestURI);

            //when
            boolean matches = requestMapper.matches(request);

            //then
            assertThat(matches).isFalse();
        }


        @Nested
        @DisplayName("RequestMapper urlPattern에 와일드카드를 사용했을 때")
        class WildCard {
            @Test
            @DisplayName("와일드카드가 마지막 세그먼트가 아니면 다른 세그먼트가 일치할 때 true를 반환한다.")
            void given_wildCard_in_the_middle_of_urlPattern_and_matched_request() {
                // given
                requestMapper = RequestMapper.permittedGetMapping("/hi/**/hello");
                request = new MockHttpServletRequest();
                request.setMethod("GET");
                request.setRequestURI("/hi/semin/hello");

                //when
                boolean matches = requestMapper.matches(request);

                //then
                assertThat(matches).isTrue();
            }

            @Test
            @DisplayName("와일드카드가 마지막 세그먼트가 아니면 다른 세그먼트가 일치하지 않을 때 false를 반환한다.")
            void given_wildCard_in_the_middle_of_urlPattern_and_unmatched_request() {
                // given
                requestMapper = RequestMapper.permittedGetMapping("/hi/**/hello");
                request = new MockHttpServletRequest();
                request.setMethod("GET");
                request.setRequestURI("/hi/name/semin");

                //when
                boolean matches = requestMapper.matches(request);

                //then
                assertThat(matches).isFalse();
            }

            @ParameterizedTest
            @ValueSource(strings = {"/hi/hello", "/hi/hello/name", "/hi/hello/name/semin"})
            @DisplayName("와일드카드가 마지막 세그먼트면 그 앞의 세그먼트가 일치할 때 어떤 요청도 true를 반환한다.")
            void given_wildCard_in_the_end_of_urlPattern_and_matched_request(String requestURI){
                // given
                requestMapper = RequestMapper.permittedGetMapping("/hi/**");
                request = new MockHttpServletRequest();
                request.setMethod("GET");
                request.setRequestURI(requestURI);

                //when
                boolean matches = requestMapper.matches(request);

                //then
                assertThat(matches).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("RequestMapper의 우선순위를 결정할 때")
    class CompareTo {
        @Test
        @DisplayName("와일드카드를 포함하는 uri가 더 낮은 우선순위를 가진다.")
        void compareTo_urlPatternWithWildcard_and_commonUrlPatter() {
            //given
            RequestMapper requestMapper1 = RequestMapper.permittedGetMapping("/hi/**");
            RequestMapper requestMapper2 = RequestMapper.permittedGetMapping("/hi/hello");

            //when
            int compare = requestMapper1.compareTo(requestMapper2);

            //then
            assertThat(compare).isGreaterThan(0);
        }

        @Test
        @DisplayName("와일드카드가 비교 대상보다 앞 쪽 세그먼트에 있는 uri가 더 낮은 우선순위를 가진다.")
        void compareTo_different_wildCard_location() {
            //given
            RequestMapper requestMapper1 = RequestMapper.permittedGetMapping("/hi/**");
            RequestMapper requestMapper2 = RequestMapper.permittedGetMapping("/hi/hello/**");
            RequestMapper requestMapper3 = RequestMapper.permittedGetMapping("/hi/**/hello/semin");

            //when
            int compare1_2 = requestMapper1.compareTo(requestMapper2);
            int compare1_3 = requestMapper1.compareTo(requestMapper3);

            //then
            assertThat(compare1_2).isGreaterThan(0);
            assertThat(compare1_3).isGreaterThan(0);
        }

        @Test
        @DisplayName("동일한 패턴의 와일드카드를 사용했다면 더 구체적은 uri가 더 높은 우선순위를 가진다")
        void compareTo_same_wildCard_pattern() {
            //given
            RequestMapper requestMapper1 = RequestMapper.permittedGetMapping("/hi/**");
            RequestMapper requestMapper2 = RequestMapper.permittedGetMapping("/hi/**/hello/semin");
            RequestMapper requestMapper3 = RequestMapper.permittedGetMapping("/hi/**/hello/*");

            //when
            int compare1_2 = requestMapper1.compareTo(requestMapper2);
            int compare1_3 = requestMapper1.compareTo(requestMapper3);

            //then
            assertThat(compare1_2).isGreaterThan(0);
            assertThat(compare1_3).isGreaterThan(0);
        }
    }
}
