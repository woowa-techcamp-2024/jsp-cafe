package camp.woowa.jspcafe.utils;

import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.utils.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SessionUtilsTest {
    @Mock
    HttpServletRequest req;
    HttpServletResponse resp;
    @Mock
    HttpSession session;

    @BeforeEach
    void setup() {
        req = org.mockito.Mockito.mock(HttpServletRequest.class);
        resp = org.mockito.Mockito.mock(HttpServletResponse.class);
        session = org.mockito.Mockito.mock(HttpSession.class);
    }

    @Test
    void testGetSession() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        User user = new User(1L, userId, password, name, email);

        when(session.getAttribute("user")).thenReturn(user);
        when(req.getSession(false)).thenReturn(session);

        // when
        Long id = SessionUtils.getSessionUser(req, resp).getId();

        // then
        assertNotNull(id);
    }
}
