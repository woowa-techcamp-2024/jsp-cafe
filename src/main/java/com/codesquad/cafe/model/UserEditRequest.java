package com.codesquad.cafe.model;

public class UserEditRequest {

    private Long id;

    private String username;

    private String password;

    private String confirmPassword;

    private String name;

    private String email;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
