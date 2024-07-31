package lass9436.user.servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lass9436.user.model.User;
import lass9436.user.model.UserRepository;
import lass9436.user.model.UserRepositoryImpl;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class UserServletTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private ServletConfig servletConfig;

	@Mock
	private ServletContext servletContext;

	@InjectMocks
	private UserServlet userServlet;

	private UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		userRepository = new UserRepositoryImpl();
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		when(servletContext.getAttribute("userRepository")).thenReturn(userRepository);
		userServlet.init(servletConfig);
	}

	@Test
	@DisplayName("doPost 성공 테스트")
	void testDoPostSuccessful() throws IOException {
		// Mock request parameters
		when(request.getParameter("userId")).thenReturn("testUser");
		when(request.getParameter("password")).thenReturn("testPassword");
		when(request.getParameter("name")).thenReturn("testName");
		when(request.getParameter("email")).thenReturn("testEmail");

		// Mock userRepository save method
		User saveUser = new User("testUser", "testPassword", "testName", "testEmail");

		// Call the servlet method
		userServlet.doPost(request, response);

		// Verify Data
		User findUser = userRepository.findByUserId("testUser");

		// saveUser 와 findUser 가 같은지 검증 (seq 를 제외하고)
		assertNotNull(findUser);
		assertEquals(saveUser.getUserId(), findUser.getUserId());
		assertEquals(saveUser.getPassword(), findUser.getPassword());
		assertEquals(saveUser.getName(), findUser.getName());
		assertEquals(saveUser.getEmail(), findUser.getEmail());

		// Verify the redirect
		verify(response).sendRedirect("/userPage?action=list");
	}

}
