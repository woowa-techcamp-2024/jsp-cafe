package codesquad.filter;

import codesquad.fixture.http.MethodFieldRequestFixture;
import codesquad.global.filter.MethodOverrideFilter;
import codesquad.http.MockRequest;
import codesquad.http.MockRequestDispatcher;
import codesquad.http.MockResponse;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;

import java.io.IOException;

class MethodOverrideFilterTest implements MethodFieldRequestFixture {
    private MethodOverrideFilter filter;
    private MockFilterChain filterChain;
    private MockRequest mockRequest;
    private MockResponse mockResponse;

    @BeforeEach
    void setUp() {
        filter = new MethodOverrideFilter();
        filterChain = new MockFilterChain();
        MockRequestDispatcher mockRequestDispatcher = new MockRequestDispatcher();
        mockRequest = new MockRequest("/something", "POST", mockRequestDispatcher);
        mockResponse = new MockResponse();
    }

    @Nested
    @DisplayName("POST 요청은")
    class PostMethodRequestIs {
        @Test
        @DisplayName("그대로 POST으로 처리된다.")
        void processAsPost() throws ServletException, IOException {
            filter.doFilter(postFieldRequest(mockRequest), mockResponse, filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "POST");
        }

        @Test
        @DisplayName("_method 필드가 PUT이면 PUT으로 처리된다.")
        void processAsPut() throws ServletException, IOException {
            filter.doFilter(putFieldRequest(mockRequest), mockResponse, filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "PUT");
        }

        @Test
        @DisplayName("_method 필드가 DELETE이면 DELETE으로 처리된다.")
        void processAsDelete() throws ServletException, IOException {
            filter.doFilter(deleteFieldRequest(mockRequest), mockResponse, filterChain);

            Assertions.assertEquals(filterChain.getRequest().getMethod(), "DELETE");
        }
    }
}
