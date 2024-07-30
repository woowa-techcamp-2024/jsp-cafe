package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.extractCookies;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.getResponse;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import org.example.cafe.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Step4 extends TomcatBaseTestEnvironment {

    private HttpURLConnection con;

    @Test
    void 올바른_아이디와_비밀번호를_입력하면_로그인할_수_있다() throws IOException {
        //given
        User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
        userRepository.save(user);

        con = createPostConnection("/login");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String urlParameters = "userId=testUser1&password=testPass";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        //when
        try (OutputStream os = con.getOutputStream()) {
            os.write(postData);
        }

        //then
        assertAll(() -> {
            assertThat(con.getResponseCode()).isEqualTo(302);
            assertThat(con.getHeaderField("Location")).isEqualTo("/");
            assertThat(extractCookies(con)).contains("SESSION");
        });
    }

    @Test
    void 아이디와_비밀번호_중_하나라도_틀리다면_로그인_실패_페이지를_반환한다() throws IOException {
        //given
        User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
        userRepository.save(user);

        con = createPostConnection("/login");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String urlParameters = "userId=testUser1&password=wrongPass";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        //when
        try (OutputStream os = con.getOutputStream()) {
            os.write(postData);
        }

        //then
        assertAll(() -> {
            assertThat(getResponse(con)).contains("아이디 또는 비밀번호가 틀립니다.");
        });
    }

    @Nested
    class 로그아웃한다 {

        @Test
        void 로그인한_사용자는_로그아웃할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createGetLoginedConnection("/logout", user);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/");
            });
        }

        @Test
        void 로그인하지_않았다면_로그인_페이지로_이동한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createGetConnection("/logout");

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/login");
            });
        }
    }

    @Nested
    class 회원_정보를_수정한다 {

        @Test
        void 로그인한_사용자는_본인의_회원_정보를_수정할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createPostLoginedConnection("/users/" + user.getUserId() + "/form", user);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "checkPassword=testPass&password=newPass&nickname=updateNick&email=update@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("update@example.com");
            });
        }

        @Test
        void 다른_회원의_정보를_수정하려_하면_403_에러를_반환한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            User other = new User("testUser2", "testPass", "testUser2", "test2@example.com");
            userRepository.save(user);
            userRepository.save(other);

            con = createPostLoginedConnection("/users/testUser2/form", user);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "checkPassword=testPass&password=newPass&nickname=updateNick&email=update@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("다른 사용자의 정보를 수정할 수 없습니다");
            });
        }

        @Test
        void 비밀번호가_일치하지_않는다면_에러_페이지로_이동한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createPostLoginedConnection("/users/" + user.getUserId() + "/form", user);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "checkPassword=notMatchPass&password=newPass&nickname=updateNick&email=update@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("비밀번호가 일치하지 않습니다");
            });
        }
    }
}
