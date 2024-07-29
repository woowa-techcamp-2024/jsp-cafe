package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("톰캣 기반 통합 테스트")
public class EndToEndTest {

    private static Tomcat tomcat;
    private static int localPort;

    private static UserRepository userRepository;
    private static QuestionRepository questionRepository;

    private HttpURLConnection con;

    @BeforeAll
    public static void setUp() throws Exception {
        String webappPath = new File("src/main/webapp").getAbsolutePath();

        tomcat = new Tomcat();
        tomcat.setPort(0);

        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));

        StandardContext context = (StandardContext) tomcat.addWebapp("", new File(webappPath).getAbsolutePath());

        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.start();

        localPort = tomcat.getConnector().getLocalPort();
        userRepository = (UserRepository) context.getServletContext().getAttribute("UserRepository");
        questionRepository = (QuestionRepository) context.getServletContext().getAttribute("QuestionRepository");
    }

    @AfterAll
    public static void tearDown() throws Exception {
        tomcat.stop();
    }

    @BeforeEach
    public void setUpEach() {
        userRepository.deleteAll();
    }

    private static HttpURLConnection createGetConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");

        return connection;
    }

    private static HttpURLConnection createPostConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        return connection;
    }

    private static String getResponse(HttpURLConnection con) throws IOException {
        InputStream inputStream = con.getInputStream();
        return new String(inputStream.readAllBytes());
    }

    private static HttpURLConnection createGetLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createGetConnection(path);
        con.setRequestProperty("Cookie", cookie);
        return con;
    }

    private static HttpURLConnection createPostLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createPostConnection(path);
        con.setRequestProperty("Cookie", cookie);
        return con;
    }

    private static String login(User user) throws Exception {
        HttpURLConnection con = createPostConnection("/login");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String urlParameters = "userId=" + user.getUserId() + "&password=" + user.getPassword();
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try (OutputStream os = con.getOutputStream()) {
            os.write(postData);
        }

        String cookie = extractCookies(con);
        con.disconnect();

        return cookie;
    }

    private static String extractCookies(HttpURLConnection con) {
        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");

        if (cookiesHeader != null) {
            StringBuilder cookieBuilder = new StringBuilder();
            for (String cookie : cookiesHeader) {
                if (cookieBuilder.length() > 0) {
                    cookieBuilder.append("; ");
                }
                String cookieValue = cookie.split(";", 2)[0];
                cookieBuilder.append(cookieValue);
            }
            return cookieBuilder.toString();
        }
        return "";
    }

    @AfterEach
    public void tearDownEach() {
        if (con != null) {
            con.disconnect();
        }
        userRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Nested
    class STEP_1 {

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
                assertThat(getResponse(con)).contains("해당하는 사용자 정보가 없습니다.");
            });
        }
    }

    @Nested
    class STEP_2 {

        @Test
        void 사용자는_게시글을_작성할_수_있다() throws IOException {
            //given
            con = createPostConnection("/questions");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "writer=test&title=test&contents=test";
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

        @Test
        void 사용자는_게시글_목록을_조회할_수_있다() throws IOException {
            //given
            questionRepository.save(new Question("title1", "content1", "writer1"));
            questionRepository.save(new Question("title2", "content2", "writer2"));

            con = createGetConnection("/");

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("title1", "title2", "writer1", "writer2");
            });
        }

        @Test
        void 사용자는_특정_게시글을_상세_조회할_수_있다() throws IOException {
            //given
            Question question = new Question("title1", "content1", "writer1");
            Long questionId = questionRepository.save(question);

            con = createGetConnection("/questions/" + questionId);

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("title1", "content1", "writer1");
            });
        }
    }

    @Nested
    class STEP_4 {

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
            void 비밀번호가_일치하지_않는다면_401_에러를_반환한다() throws Exception {
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
}
