package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createDeleteConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createDeleteLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createGetLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPostLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPutConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.createPutLoginedConnection;
import static org.example.cafe.e2e.HttpUrlConnectionUtils.getResponse;
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
class Step5 extends TomcatBaseTestEnvironment {

    private HttpURLConnection con;

    @Nested
    class 게시글을_작성한다 {
        @Test
        void 로그인하지_않은_사용자는_게시글_작성_폼_요청_시_로그인_페이지로_이동한다() throws IOException {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createGetConnection("/questions");

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/login");
            });
        }

        @Test
        void 로그인하지_않은_사용자는_게시글_작성_시_로그인_페이지로_이동한다() throws IOException {
            //given
            con = createPostConnection("/questions");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "title=test&contents=test";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

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
        void 로그인한_사용자는_게시글_작성_폼을_요청할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            con = createGetLoginedConnection("/questions", user);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
            });
        }

        @Test
        void 로그인한_사용자는_게시글을_작성할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);

            //given
            con = createPostLoginedConnection("/questions", user);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "title=test&contents=test";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(302);
                assertThat(con.getHeaderField("Location")).isEqualTo("/");
            });
        }
    }

    @Nested
    class 게시글을_삭제한다 {

        @Test
        void 로그인하지_않은_사용자는_게시글_삭제_요청_시_401_에러가_발생한다() throws IOException {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createDeleteConnection("/questions/" + savedQuestionId);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(401);
            });
        }

        @Test
        void 작성자가_아닌_사용자가_게시글_삭제_요청_시_에러_페이지로_이동한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            User other = new User("testUser2", "testPass", "testUser2", "test2@example.com");
            userRepository.save(user);
            userRepository.save(other);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createDeleteLoginedConnection("/questions/" + savedQuestionId, other);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(403);
            });
        }

        @Test
        void 작성자인_사용자는_게시글을_삭제할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createDeleteLoginedConnection("/questions/" + savedQuestionId, user);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
            });
        }
    }

    @Nested
    class 게시글을_수정한다 {

        @Test
        void 작성자가_아닌_사용자는_게시글_수정_폼_요청_시_에러_페이지로_이동한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            User other = new User("testUser2", "testPass", "testUser2", "test2@example.com");
            userRepository.save(user);
            userRepository.save(other);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createGetLoginedConnection("/questions/" + savedQuestionId + "?edit=true", other);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("작성자만 수정, 삭제할 수 있습니다");
            });
        }

        @Test
        void 로그인하지_않은_사용자는_게시글_수정_요청_시_401_에러가_발생한다() throws IOException {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createPutConnection("/questions/" + savedQuestionId);
            con.setRequestProperty("Content-Type", "application/json");
            String requestBody =
                    "{\"questionId\":\"" + savedQuestionId + "\",\"title\":\"newTitle\",\"contents\":\"newContent\"}";
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(401);
            });
        }

        @Test
        void 작성자가_아닌_사용자가_게시글_수정_요청_시_403_에러가_발생한다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            User other = new User("testUser2", "testPass", "testUser2", "test2@example.com");
            userRepository.save(user);
            userRepository.save(other);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createPutLoginedConnection("/questions/" + savedQuestionId, other);
            con.setRequestProperty("Content-Type", "application/json");
            String requestBody =
                    "{\"questionId\":\"" + savedQuestionId + "\",\"title\":\"newTitle\",\"contents\":\"newContent\"}";
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(403);
            });
        }

        @Test
        void 작성자인_사용자는_게시글을_수정할_수_있다() throws Exception {
            //given
            User user = new User("testUser1", "testPass", "testUser1", "test@example.com");
            userRepository.save(user);
            Question question = new Question("title", "content", user.getUserId());
            Long savedQuestionId = questionRepository.save(question);

            con = createPutLoginedConnection("/questions/" + savedQuestionId, user);
            con.setRequestProperty("Content-Type", "application/json");
            String requestBody =
                    "{\"questionId\":\"" + savedQuestionId + "\",\"title\":\"newTitle\",\"contents\":\"newContent\"}";
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = con.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
            });
        }
    }
}