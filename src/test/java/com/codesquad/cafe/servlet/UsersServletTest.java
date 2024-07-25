package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.model.User;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private final String path = "/users";

    private User user;

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.of("woowa", "1234", "김수현", "woowa@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 목록 페이지를 반환한다.")
    void testDoGetUserJoinForm() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(path);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("전체 사용자 1명"));
        assertTrue(html.contains("<a href=\"/users/"));
        assertTrue(html.contains("woowa"));
    }


    @Test
    @DisplayName("POST 요청시 405 응답을 반환한다.")
    void testDoPostReturn405() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post("/", "");

        //then
        assertErrorPageResponse(response, 405);
    }
}
