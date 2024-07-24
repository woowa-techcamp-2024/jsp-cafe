package cafe.domain.entity;

public class User {
    private final String id;
    private final String password;
    private final String name;
    private final String email;

    public static User of(String id, String password, String name, String email) {
        return new User(id, password, name, email);
    }

    private User(String id, String password, String name, String email) {
        validate(id, password, name, email);
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
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
