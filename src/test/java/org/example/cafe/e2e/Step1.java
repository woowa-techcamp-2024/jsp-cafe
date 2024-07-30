package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.getResponse;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.example.cafe.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Step1 extends TomcatBaseTestEnvironment {

    private HttpURLConnection con;

    @Test
    void 존재하지_않는_회원의_프로필은_조회할_수_없다() throws Exception {
        //given
        userRepository.save(new User("없는유저1", "testPass", "testUser1", "testEmail1@test.com"));

        con = createGetConnection("/users/" + URLEncoder.encode("테스트유저1", StandardCharsets.UTF_8));

        //when
        con.connect();

        //then
        assertAll(() -> {
            assertThat(con.getResponseCode()).isEqualTo(200);
            assertThat(getResponse(con)).contains("사용자를 찾을 수 없습니다.");
        });
    }

    @Test
    void 사용자는_회원_목록을_조회할_수_있다() throws Exception {
        //given
        userRepository.save(new User("testUser1", "testPass", "testUser1", "testEmail1@test.com"));
        userRepository.save(new User("testUser2", "testPass", "testUser2", "testEmail2@test.com"));

        con = createGetConnection("/users");

        //when
        con.connect();

        //then
        assertAll(() -> {
            assertThat(con.getResponseCode()).isEqualTo(200);
            assertThat(getResponse(con)).contains("testUser1", "testUser2");
        });
    }

    @Test
    void 사용자는_회원의_프로필을_조회할_수_있다() throws Exception {
        //given
        userRepository.save(new User("테스트유저1", "testPass", "testUser1", "testEmail1@test.com"));

        con = createGetConnection("/users/" + URLEncoder.encode("테스트유저1", StandardCharsets.UTF_8));

        //when
        con.connect();

        //then
        assertAll(() -> {
            assertThat(con.getResponseCode()).isEqualTo(200);
            assertThat(getResponse(con)).contains("테스트유저1", "testEmail1@test.com");
        });
    }

    @Nested
    class 회원가입한다 {

        @Test
        void 사용자는_회원가입할_수_있다() throws Exception {
            //given
            con = createPostConnection("/users");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "userId=testUser&password=testPass&nickname=testNick&email=test@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/users");
            });
        }

        @Test
        void 이미_사용자id가_존재한다면_400_에러를_반환한다() throws Exception {
            //given
            userRepository.save(new User("testUser1", "testPass", "testUser1", "testEmail1@test.com"));

            con = createPostConnection("/users");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "userId=testUser1&password=testPass&nickname=testNick&email=test@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("이미 사용 중인 아이디입니다");
            });
        }
    }
}
