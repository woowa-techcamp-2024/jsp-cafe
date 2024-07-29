package cafe.domain.entity;

public class User {
    private final String userid;
    private final String name;
    private final String password;
    private final String email;

    public static User of(String id, String name, String password, String email) {
        return new User(id, name, password, email);
    }

    private User(String id, String name, String password, String email) {
        validate(id, password, name, email);
        this.userid = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getUserid() {
        return userid;
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

    public void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id is empty!");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is empty!");
        }
    }

    public void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is empty!");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is empty!");
        }
    }

    public void validate(String id, String password, String name, String email) {
        validateId(id);
        validatePassword(password);
        validateName(name);
        validateEmail(email);
    }
}
