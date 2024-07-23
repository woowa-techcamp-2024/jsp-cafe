package codesquad.jspcafe.domain.user.payload.response;

import codesquad.jspcafe.domain.user.domain.User;

public class UserCommonResponse {

    private final String userId;
    private final String username;
    private final String email;

    private UserCommonResponse(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public static UserCommonResponse from(User user) {
        return new UserCommonResponse(user.getUserId(), user.getUsername(),
            user.getEmail().getValue());
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
