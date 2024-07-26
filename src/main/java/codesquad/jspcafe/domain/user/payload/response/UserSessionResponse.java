package codesquad.jspcafe.domain.user.payload.response;

import codesquad.jspcafe.domain.user.domain.User;
import java.io.Serial;
import java.io.Serializable;

public class UserSessionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 230497820397842L;

    private final Long id;
    private final String userId;
    private final String username;
    private final String email;

    private UserSessionResponse(Long id, String userId, String username, String email) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public static UserSessionResponse from(User user) {
        return new UserSessionResponse(user.getId(), user.getUserId(), user.getUsername(),
            user.getEmail());
    }

    public Long getId() {
        return id;
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
