package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostLoginedConnection;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Step6 extends TomcatBaseTestEnvironment {

    private HttpURLConnection con;

    @Nested
    class 댓글을_작성한다 {

        @Test
        void 로그인하지_않은_사용자는_댓글_작성_시_로그인_페이지로_이동한다() throws IOException {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createPostConnection("/replies");
            con.setRequestProperty("Content-Type", "application/json");
            String requestBody =
                    "{\"questionId\": " + savedQuestionId + ",\"content\":\"newContent\"}";
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/login");
            });
        }

        @Test
        void 로그인한_사용자는_댓글을_작성할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createPostLoginedConnection("/replies", user);
            con.setRequestProperty("Content-Type", "application/json");
            String requestBody =
                    "{\"questionId\":" + savedQuestionId + ",\"content\":\"newContent\"}";
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/questions/" + savedQuestionId);
            });
        }
    }
}