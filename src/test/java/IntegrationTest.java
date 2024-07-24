import cafe.AppContextListener;
import cafe.Factory;
import cafe.users.User;
import cafe.users.UserRepository;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {

    private static Tomcat tomcat;
    private static int port = 8080;
    private static Factory factory = new TestFactory();

    @BeforeAll
    public static void setUp() throws Exception {
        tomcat = buildTomcat();

        tomcat.start();
        port = tomcat.getConnector().getLocalPort();

        System.out.println("Tomcat started on port: " + port);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        tomcat.stop();
        tomcat.destroy();
    }

    private static Tomcat buildTomcat() {
        tomcat = new Tomcat();
        tomcat.setPort(0);
        tomcat.getConnector();

        // 웹 애플리케이션 루트를 설정합니다.
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

        // 리스너를 추가합니다.
        ctx.addApplicationListener(TestApplicationListener.class.getName());

        Tomcat.addServlet(ctx, "jsp", "org.apache.jasper.servlet.JspServlet");
        ctx.addServletMappingDecoded("*.jsp", "jsp");

        return tomcat;
    }

    public static class TestApplicationListener extends AppContextListener {
        public TestApplicationListener() {
            super(factory);
        }
    }

    static class TestFactory extends Factory {
    }

    @Test
    public void testUserServlet() throws IOException, InterruptedException, URISyntaxException {
        var client = java.net.http.HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/users"))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testUserRegisterServlet() throws IOException, InterruptedException, URISyntaxException {
        String username = "test" + System.currentTimeMillis();
        String email = "test@mail.com" + System.currentTimeMillis();
        String password = "password" + System.currentTimeMillis();
        String confirmPassword = password;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/users"))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        String.format("username=%s&email=%s&password=%s&confirmPassword=%s", username, email, password, confirmPassword)
                ))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(302, response.statusCode());
        assertEquals("/users", response.headers().firstValue("Location").orElse(null));

        UserRepository userRepository = factory.userRepository();

        assertTrue(userRepository.findAll().stream().anyMatch(user ->
                user.getUsername().equals(username) && user.getUserId().equals(email) && user.getPassword().equals(password)
        ));
    }

    @Test
    void 회원Profile_페이지_테스트() throws IOException, InterruptedException, URISyntaxException {
        String username = "test" + System.currentTimeMillis();
        String email = "test" + System.currentTimeMillis() + "@mail.com";
        String password = "password" + System.currentTimeMillis();

        User user = factory.userRepository().save(new User(email, username, password));
        Long id = user.getId();

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/users/" + id))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(username));
        assertTrue(response.body().contains(email));
    }

    @Test
    void 질문등록_테스트() throws IOException, InterruptedException, URISyntaxException {
        String title = "질문 제목" + System.currentTimeMillis();
        String content = "질문 내용" + System.currentTimeMillis();

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:" + port + "/question/write"))
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        String.format("title=%s&content=%s", title, content)
                ))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(302, response.statusCode());
        assertEquals("/questions", response.headers().firstValue("Location").orElse(null));
    }

    @Test
    void 질문내용부족_실패테스트() throws IOException, InterruptedException {
        String title = "질문 제목" + System.currentTimeMillis();
        String content = "";

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/question/write"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        String.format("title=%s&content=%s", title, content)
                ))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("제목과 내용을 모두 입력해주세요."));
    }

}
