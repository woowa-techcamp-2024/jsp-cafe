package org.example.cafe.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.example.cafe.domain.user.User;
import org.example.cafe.domain.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
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

    @BeforeAll
    public static void setUp() throws Exception {
        String webappPath = new File("src/main/webapp").getAbsolutePath();

        tomcat = new Tomcat();
        tomcat.setPort(0);

        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));

        StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappPath).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappPath).getAbsolutePath());

        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.start();

        localPort = tomcat.getConnector().getLocalPort();
        userRepository = (UserRepository) context.getServletContext().getAttribute("UserRepository");
    }

    @AfterAll
    public static void tearDown() throws Exception {
        tomcat.stop();
    }

    @BeforeEach
    public void setUpEach() {
        userRepository.deleteAll();
    }

    @Nested
    class STEP_1 {

        @Test
        void 사용자는_회원가입할_수_있다() throws Exception {
            //given
            URL url = new URL("http://localhost:" + localPort + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String urlParameters = "userId=testUser&password=testPass&nickname=testNick&email=test@example.com";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            //when
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData);
            }

            //then
            assertAll(() -> {
                assertThat(connection.getResponseCode()).isEqualTo(302);
                assertThat(connection.getHeaderField("Location")).isEqualTo("/users");
            });

            connection.disconnect();
        }

        @Test
        void 사용자는_회원_목록을_조회할_수_있다() throws Exception {
            //given
            userRepository.save(new User("testUser1", "testPass", "testUser1", "testEmail1@test.com"));
            userRepository.save(new User("testUser2", "testPass", "testUser2", "testEmail2@test.com"));

            URL url = new URL("http://localhost:" + localPort + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            //when
            connection.connect();

            //then
            assertAll(() -> {
                assertThat(connection.getResponseCode()).isEqualTo(200);

                InputStream inputStream = connection.getInputStream();
                String response = new String(inputStream.readAllBytes());

                assertThat(response).contains("testUser1", "testUser2");
            });

            connection.disconnect();
        }
    }
}
