package org.example.cafe.e2e;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.example.cafe.domain.User;

public final class HttpUrlConnectionUtils {

    private static int localPort;

    public static void setLocalPort(int localPort) {
        HttpUrlConnectionUtils.localPort = localPort;
    }

    public static HttpURLConnection createGetConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");

        return connection;
    }

    public static HttpURLConnection createPostConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        return connection;
    }

    public static HttpURLConnection createPutConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        return connection;
    }

    public static HttpURLConnection createDeleteConnection(String path) throws IOException {
        URL url = new URL("http://localhost:" + localPort + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("DELETE");

        return connection;
    }

    public static String getResponse(HttpURLConnection con) throws IOException {
        InputStream inputStream = con.getInputStream();
        return new String(inputStream.readAllBytes());
    }

    public static HttpURLConnection createGetLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createGetConnection(path);
        con.setRequestProperty("Cookie", cookie);
        return con;
    }

    public static HttpURLConnection createPostLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createPostConnection(path);
        con.setRequestProperty("Cookie", cookie);
        return con;
    }

    public static HttpURLConnection createPutLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createPutConnection(path);
        con.setRequestProperty("Cookie", cookie);

        return con;
    }

    public static HttpURLConnection createDeleteLoginedConnection(String path, User user) throws Exception {
        String cookie = login(user);

        HttpURLConnection con = createDeleteConnection(path);
        con.setRequestProperty("Cookie", cookie);

        return con;
    }

    public static String login(User user) throws Exception {
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

    public static String extractCookies(HttpURLConnection con) {
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
}
