package cafe.users;

public class User {
    private final Long id;
    private final String userId;
    private final String email;
    private final String username;
    private final String password;

    public User(String userId, String email, String username, String password) {
        this(null, userId, email, username, password);
    }

    public User(Long id, String userId, String email, String username, String password) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User withId(Long id) {
        return new User(id, userId, email, username, password);
    }

    public User withPassword(String password) {
        return new User(id, userId, email, username, password);
    }

    public User withUsername(String username) {
        return new User(id, userId, email, username, password);
    }

    public User withEmail(String email) {
        return new User(id, userId, email, username, password);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
