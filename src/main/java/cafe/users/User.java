package cafe.users;

public class User {
    private final Long id;
    private final String userId;
    private final String username;
    private final String password;

    public User(String userId, String username, String password) {
        this(null, userId, username, password);
    }

    public User(Long id, String userId, String username, String password) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public User withId(Long id) {
        return new User(id, this.userId, this.username, this.password);
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

    public String getPassword() {
        return password;
    }
}
