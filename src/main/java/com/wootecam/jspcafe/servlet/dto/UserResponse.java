package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.User;

public class UserResponse {

    private Long id;
    private final String userId;
    private final String name;
    private final String email;

    public UserResponse(final Long id, final String userId, final String name, final String email) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUserId(), user.getName(), user.getEmail());
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
