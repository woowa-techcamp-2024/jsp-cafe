package camp.woowa.jspcafe.models;

public class User {
    private final Long id;
    private String userId;
    private final String password;
    private String name;
    private String email;

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public void update(String userId, String updatedName, String updatedEmail) {
        this.userId = userId;
        this.name = updatedName;
        this.email = updatedEmail;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}
