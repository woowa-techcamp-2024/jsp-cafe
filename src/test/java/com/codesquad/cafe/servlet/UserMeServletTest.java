package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.model.dto.ErrorResponse;
import com.codesquad.cafe.model.dto.RedirectResponse;
import com.codesquad.cafe.model.dto.UserEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMeServletTest extends E2ETestBase {

    private static final String path = "/me";

    private static UserRepository userRepository;

    private static ObjectMapper objectMapper;

    private User user;

    private String sessionId;


    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() throws IOException {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "park@gmail.com"));
        sessionId = getSessionIdFromSetCookieHeader(
                post("/login", "username=javajigi&password=1234").getFirstHeader("Set-Cookie").getValue());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 프로필 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("Profile"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("인증 없이 유저 프로필 요청시 로그인 페이지로 리다이렉트한다.")
    void testDoGetUserProfileUnauthorized() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(path);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("POST 요청시 405 응답을 반환한다.")
    void testDoPostReturn405() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path, "", sessionId);

        //then
        assertErrorPageResponse(response, 405);
    }


    @Test
    @DisplayName("유저 수정에 성공하면 리다이렉트 주소를 반환한다.")
    void testDoPostEditUser() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com",
                        "박재성"),
                sessionId);

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("/me",
                objectMapper.readValue(response.getBody(), RedirectResponse.class).getRedirect());
        User updatedUser = userRepository.findById(user.getId()).get();
        assertEquals("woo@gmail.com", updatedUser.getEmail());
        assertEquals("1234", updatedUser.getPassword());
    }

    @Test
    @DisplayName("세션 쿠키 없이 유저 수정 요청시 로그인페이지로 리다이렉트한다.")
    void testDoPostEditUserWithoutSessionCookie() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com",
                        "박재성"), null);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("세션 쿠키와 다른 유저 수정 요청시 성공하면 403 응답한다.")
    void testDoPostEditNotMe() throws Exception {
        //given
        User otherUser = userRepository.save(User.of("wow", "1234", "박재성", "test@gmail.com"));
        String otherSessionId = getSessionIdFromSetCookieHeader(
                post("/login", "username=wow&password=1234").getFirstHeader("Set-Cookie").getValue());

        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com",
                        "박재성"),
                otherSessionId);

        //then
        assertErrorPageResponse(response, 403);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 비밀번호 불일치")
    void testDoPutEditUserFailPassword() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "1234", "1111", "woo@gmail.com",
                        "박재성"),
                sessionId);
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertTrue(response.getContentType().contains("json"));
        assertEquals("비밀번호가 비밀번호 확인과 일치하지 않습니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 기존 비밀번호 오류")
    void testDoPutEditUserWrongOriginalPassword() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), "wkjrwlk", "1111", "1111", "woo@gmail.com",
                        "박재성"),
                sessionId);
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertTrue(response.getContentType().contains("json"));
        assertTrue(errorResponse.getMessage().contains("기존 비밀번호"));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 빈 비밀번호")
    void testDoPostEditUserFailPasswordEmpty() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "", "", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    @DisplayName("유저 이름은 수정할 수 없다.")
    void testDoPostEditUserFailEditUsername() throws Exception {
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), "nweUsername", user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertTrue(response.getContentType().contains("json"));
        assertNotNull(errorResponse.getMessage());
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 탈퇴한 유저")
    void testDoPostEditUserFailDeletedUser() throws Exception {
        //given
        deleteUser(user);
        //when
        SavedHttpResponse response = put(path,
                getBodyJson(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com",
                        "박재성"),
                sessionId);
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertTrue(response.getContentType().contains("json"));
        assertNotNull(errorResponse.getMessage());
    }

    public void deleteUser(User user) {
        user.delete();
        userRepository.save(user);
    }

    public String getBodyJson(Long id, String username, String originalPassword, String password,
                              String confirmPassword,
                              String email, String name)
            throws IOException {
        return objectMapper.writeValueAsString(
                new UserEditRequest(id, username, password, originalPassword, confirmPassword, name, email));
    }

}
