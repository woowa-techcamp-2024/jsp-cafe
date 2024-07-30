package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.User;
import java.io.IOException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserEditServletTest extends E2ETestBase {

    private static String path = "/users/edit";

    private static UserRepository userRepository;

    private User user;

    private String sessionId;

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
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
    @DisplayName("유저 수정 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException {
        //when
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("세션 쿠키 없이 수정 페이지를 요청하면 로그인 페이지로 리다이렉트한다.")
    void testDoGetUnknownUser() throws IOException {
        //when
        SavedHttpResponse response = get(path);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("유저 수정에 성공하면 유저 리스트로 리다이렉트한다.")
    void testDoPostEditUser() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/users/" + user.getId(), response.getFirstHeader("Location").getValue());
        User updatedUser = userRepository.findById(user.getId()).get();
        assertEquals("woo@gmail.com", updatedUser.getEmail());
        assertEquals("1234", updatedUser.getPassword());
    }

    @Test
    @DisplayName("세션 쿠키 없이 유저 수정 요청시 401 응답한다.")
    void testDoPostEditUserWithoutSessionCookie() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/users/" + user.getId(), response.getFirstHeader("Location").getValue());
        User updatedUser = userRepository.findById(user.getId()).get();
        assertEquals("woo@gmail.com", updatedUser.getEmail());
        assertEquals("1234", updatedUser.getPassword());
    }

    @Test
    @DisplayName("세션 쿠키와 다른 유저 수정 요청시 성공하면 401 응답한다.")
    void testDoPostEditNotMe() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(100L, user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertErrorPageResponse(response, 401);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 비밀번호 불일치")
    void testDoPostEditUserFailPassword() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "1234", "1111", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("비밀번호가 비밀번호 확인과 일치하지 않습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 필드 누락")
    void testDoPostEditUserFailFieldAbsent() throws IOException {
        //when
        HttpResponse response = post(path,
                "id=" + user.getId() + "&username=" + user.getUsername() + "&password=1234" + "&confirmPassword=1234",
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 빈 필드")
    void testDoPostEditUserFailFieldEmpty() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "", "박재성"),
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 잘못된 기존 비밀번호")
    void testDoPostEditUserFailWrong() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "wrongpw", "12345", "12345", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("기존 비밀번호가 일치하지 않습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 빈 비밀번호")
    void testDoPostEditUserFailPasswordEmpty() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "", "", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 이름은 수정할 수 없다.")
    void testDoPostEditUserFailEditUsername() throws IOException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), "nweUsername", user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("사용자 아이디는 변경할 수 없습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 탈퇴한 유저")
    void testDoPostEditUserFailDeletedUser() throws IOException {
        //given
        deleteUser(user);
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), user.getPassword(), "1234", "1234", "woo@gmail.com", "박재성"),
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    public void deleteUser(User user) {
        user.delete();
        userRepository.save(user);
    }

    public String getBody(Long id, String username, String originalPassword, String password, String confirmPassword,
                          String email, String name)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb
                .append("id=").append(id)
                .append("&username=").append(username)
                .append("&originalPassword=").append(originalPassword)
                .append("&password=").append(password)
                .append("&confirmPassword=").append(confirmPassword)
                .append("&name=").append(name)
                .append("&email=").append(URLEncoder.encode(email, "UTF-8"));
        return sb.toString();
    }


}
