package codesquad.jspcafe.domain.user.payload.request;

import java.util.Map;

public class UserUpdateRequest {

    private final String userId;
    private final String password;
    private String username;
    private String email;

    private UserUpdateRequest(String userId, String password, String username, String email) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public static UserUpdateRequest from(Map<String, String[]> map) {
        String userId = map.get("userId")[0];
        String password = map.get("password")[0];
        String username = map.getOrDefault("username", new String[1])[0];
        String email = map.getOrDefault("email", new String[1])[0];
        return new UserUpdateRequest(userId, password, username, email);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
