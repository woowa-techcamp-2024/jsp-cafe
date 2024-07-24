package codesquad.domain.user;

public class User {
    private Long id;
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Long id, User user) {
        this.id = id;
        this.userId = user.userId;
        this.password = user.password;
        this.name = user.name;
        this.email = user.email;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void update(String password, String name, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
