package codesquad.jspcafe.domain.user.payload.request;

import java.util.Map;

public class UserLoginRequest {

    private final String userId;
    private final String password;

    private UserLoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public static UserLoginRequest from(Map<String, String[]> map) {
        return new UserLoginRequest(map.get("userId")[0], map.get("password")[0]);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
