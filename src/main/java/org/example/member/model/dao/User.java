package org.example.member.model.dao;

public class User {

    private String userId;
    private String password;
    private String name;
    private String email;

    public static User createUser(String userId, String password, String name, String email) {
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.name = name;
        user.email = email;

        return user;
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

    private void validate() {

    }

    @Override
    public String toString() {
        return "Member{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
