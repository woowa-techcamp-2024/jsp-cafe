package codesquad.jspcafe.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultServlet은")
class DefaultServletTest {

    @InjectMocks
    private DefaultServlet defaultServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Test
    @DisplayName("GET 요청을 처리하여 .html 을 .jsp 로 치환 및 포워딩한다.")
    void doGet() throws ServletException, IOException {
        // Arrange
        String path = "/index.html";
        String expectedPath = "/index.jsp";
        given(request.getRequestURI()).willReturn(path);
        given(request.getRequestDispatcher(expectedPath)).willReturn(requestDispatcher);
        // Act
        defaultServlet.doGet(request, response);
        // Assert
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(request).getRequestDispatcher(captor.capture());
        assertThat(expectedPath).isEqualTo(captor.getValue());
        verify(requestDispatcher).forward(request, response);
    }

}