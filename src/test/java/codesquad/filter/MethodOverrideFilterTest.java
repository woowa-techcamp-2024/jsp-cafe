package codesquad.filter;

import codesquad.fixture.http.MethodFieldRequestFixture;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;

import java.io.IOException;

class MethodOverrideFilterTest implements MethodFieldRequestFixture {
    private MethodOverrideFilter filter;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new MethodOverrideFilter();
        filterChain = new MockFilterChain();
    }

    @Nested
    @DisplayName("POST 요청은")
    class PostMethodRequestIs {
        @Test
        @DisplayName("그대로 POST으로 처리된다.")
        void processAsPost() throws ServletException, IOException {
            filter.doFilter(postRequest(), emptyResponse(), filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "POST");
        }

        @Test
        @DisplayName("_method 필드가 PUT이면 PUT으로 처리된다.")
        void processAsPut() throws ServletException, IOException {
            filter.doFilter(putFieldRequest(), emptyResponse(), filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "PUT");
        }

        @Test
        @DisplayName("_method 필드가 DELETE이면 DELETE으로 처리된다.")
        void processAsDelete() throws ServletException, IOException {
            filter.doFilter(deleteFieldRequest(), emptyResponse(), filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "DELETE");
        }
    }
}