package codesquad.jspcafe.filter;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExceptionHandlingFilter는")
class ExceptionHandlingFilterTest {

    private final ExceptionHandlingFilter exceptionHandlingFilter = new ExceptionHandlingFilter();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private FilterChain filterChain;

    @Nested
    @DisplayName("doFilter 메서드는")
    class doFilterMethodDo {

        @Test
        @DisplayName("예외가 발생하지 않은 경우 다음 필터로 요청을 전달한다.")
        void doFilterWhenNoException() throws Exception {
            // Act
            exceptionHandlingFilter.doFilter(request, response, filterChain);
            // Assert
            verify(filterChain).doFilter(request, response);
        }

        @Nested
        @DisplayName("예외가 발생한 경우")
        class whenExceptionRaise {

            @ParameterizedTest
            @MethodSource("exceptionProvider")
            @DisplayName("예외를 처리하여 에러 페이지로 포워딩한다.")
            void doFilterWhenExceptionRaised(Exception exception, int statusCode) throws Exception {
                // Arrange
                doThrow(exception).when(filterChain).doFilter(request, response);
                given(request.getRequestDispatcher(any(String.class))).willReturn(
                    requestDispatcher);
                given(request.getMethod()).willReturn("GET");
                // Act
                exceptionHandlingFilter.doFilter(request, response, filterChain);
                // Assert
                verify(request).setAttribute("message", exception.getMessage());
                verify(request).setAttribute("statusCode", statusCode);
                verify(requestDispatcher).forward(request, response);
            }

            @ParameterizedTest
            @MethodSource("exceptionProvider")
            @DisplayName("예외를 처리하여 예외 응답을 남긴다.")
            void doFilterWhenExceptionRaisedSetStatus(Exception exception, int statusCode)
                throws Exception {
                // Arrange
                doThrow(exception).when(filterChain).doFilter(request, response);
                given(response.getWriter()).willReturn(printWriter);
                given(request.getMethod()).willReturn("PUT");
                // Act
                exceptionHandlingFilter.doFilter(request, response, filterChain);
                // Assert
                verify(response).setStatus(statusCode);
                verify(printWriter).write(exception.getMessage());
            }

            private static Stream<Arguments> exceptionProvider() {
                return Stream.of(
                    Arguments.of(new AccessDeniedException("Access Denied"), 403),
                    Arguments.of(new NoSuchElementException("No Such Element"), 404),
                    Arguments.of(new IllegalArgumentException("Illegal Argument"), 400),
                    Arguments.of(new SecurityException("Security Exception"), 401)
                );
            }
        }
    }
}