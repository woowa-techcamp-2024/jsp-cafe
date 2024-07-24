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

        StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappPath).getAbsolutePath());

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

    @AfterEach
    public void tearDownEach() {
        if (con != null) {
            con.disconnect();
        }
    }

    @Nested
    class STEP_1 {

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
            questionRepository.save(new Question("title1", "content1", "writer1"));
            questionRepository.save(new Question("title2", "content2", "writer2"));

            //given
            con = createGetConnection("/");

            //when
            con.connect();

            //then
            assertAll(() -> {
                assertThat(con.getResponseCode()).isEqualTo(200);
                assertThat(getResponse(con)).contains("title1", "title2", "writer1", "writer2");
            });
        }
    }
}
