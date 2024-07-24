package cafe.users;

public record User(Long id, String userId, String username, String password) {
    public User(String userId, String username, String password) {
        this(null, userId, username, password);
    }

    public User {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
    }

    public User withId(Long id) {
        return new User(id, this.userId, this.username, this.password);
    }
}
