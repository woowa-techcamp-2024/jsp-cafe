package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private final String path = "/logout";

    private User user;

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "test@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그아웃시 세션을 만료하고 새로운 세션 쿠키를 생성한다.")
    void testDoGetLoginForm() throws IOException {
        //given
        HttpResponse loginResponse = post("/login",
                "username=" + user.getUsername() + "&password=" + user.getPassword());
        String originalSessionId = getSessionIdFromSetCookieHeader(
                loginResponse.getFirstHeader("Set-Cookie").getValue());

        //when
        SavedHttpResponse savedResponse = get(path, originalSessionId);
        //then
        assertEquals(302, savedResponse.getStatusLine().getStatusCode());
        assertEquals("/", savedResponse.getFirstHeader("Location"));
    }
}
