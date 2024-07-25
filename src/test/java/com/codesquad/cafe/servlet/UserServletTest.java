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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServletTest extends E2ETestBase {

    private static UserRepository userRepository;

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
    @DisplayName("유저 프로필 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(user.getId()));
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("Profile"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("없는 유저를 조회하면 404 페이지를 응답한다.")
    void testDoGetUnknownUser() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(100000L));

        //then
        assertErrorPageResponse(response, 404);
    }

    private String getPath(Long id) {
        return "/users/" + id;
    }

}
