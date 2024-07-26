package org.example.demo.model;

public class UserUpdateDao {
    private Long id;
    private String password;
    private String name;
    private String email;

    public UserUpdateDao(Long id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public Long getId() {
        return id;
    }
}
