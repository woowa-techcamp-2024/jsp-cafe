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

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.of("woowa", "1234", "박재성", "woowa@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 수정 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(user.getId()));
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("없는 유저의 수정 페이지를 요처하면404 페이지를 응답한다.")
    void testDoGetUnknownUser() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(100000L));

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("탈퇴한 유저의 유저의 수정 페이지를 요처하면404 페이지를 응답한다.")
    void testDoGetDeletedUser() throws IOException, URISyntaxException {
        //given
        deleteUser(user);
        //when
        SavedHttpResponse response = get(getPath(100000L));

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 수정에 성공하면 유저 리스트로 리다이렉트한다.")
    void testDoPostEditUser() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "1234", "1234", "woo@gmail.com", "박재성"));

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/users/" + user.getId(), response.getFirstHeader("Location").getValue());
        User updatedUser = userRepository.findById(user.getId()).get();
        assertEquals("woo@gmail.com", updatedUser.getEmail());
        assertEquals("1234", updatedUser.getPassword());
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 비밀번호 불일치")
    void testDoPostEditUserFailPassword() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "1234", "1111", "woo@gmail.com", "박재성"));

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("비밀번호가 일치하지 않습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 필드 누락")
    void testDoPostEditUserFailFieldAbsent() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                "id=" + user.getId() + "&username=" + user.getUsername() + "&password=1234" + "&confirmPassword=1234");

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 빈 필드")
    void testDoPostEditUserFailFieldEmpty() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "1234", "1234", "", "박재성"));

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 빈 비밀번호")
    void testDoPostEditUserFailPasswordEmpty() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "", "", "woo@gmail.com", "박재성"));

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("비밀번호가 일치하지 않습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 이름은 수정할 수 없다.")
    void testDoPostEditUserFailEditUsername() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), "nweUsername", "1234", "1234", "woo@gmail.com", "박재성"));

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("유저 이름은 변경할 수 없습니다."));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("유저 수정에 실패한다 - 탈퇴한 유저")
    void testDoPostEditUserFailDeletedUser() throws IOException, URISyntaxException {
        //given
        deleteUser(user);
        //when
        HttpResponse response = post(path,
                getBody(user.getId(), user.getUsername(), "1234", "1234", "woo@gmail.com", "박재성"));

        //then
        assertErrorPageResponse(response, 400);
    }

    public void deleteUser(User user) {
        user.delete();
        userRepository.save(user);
    }

    public String getPath(Long id) {
        return path + "?id=" + id;
    }

    public String getBody(Long id, String username, String password, String confirmPassword, String email, String name)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb
                .append("id=").append(id)
                .append("&username=").append(username)
                .append("&password=").append(password)
                .append("&confirmPassword=").append(confirmPassword)
                .append("&name=").append(name)
                .append("&email=").append(URLEncoder.encode(email, "UTF-8"));
        return sb.toString();
    }


}
