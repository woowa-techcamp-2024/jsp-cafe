package com.codesquad.cafe.model;

public class UserEditRequest {

    private Long id;

    private String username;

    private String password;

    private String originalPassword;

    private String confirmPassword;

    private String name;

    private String email;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getOriginalPassword() {
        return originalPassword;
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

    public boolean isValid() {
        if (id == null || id <= 0) {
            return false;
        }
        if (username == null || username.isBlank()) {
            return false;
        }
        if (password == null || password.isBlank()) {
            return false;
        }
        if (originalPassword == null || originalPassword.isBlank()) {
            return false;
        }
        if (confirmPassword == null || confirmPassword.isBlank()) {
            return false;
        }
        if (name == null || name.isBlank()) {
            return false;
        }
        if (email == null || email.isBlank()) {
            return false;
        }
        String[] emailTokens = email.split("@");
        if (emailTokens.length != 2 || emailTokens[0].isBlank() || emailTokens[1].isBlank()) {
            return false;
        }
        return true;
    }

}
