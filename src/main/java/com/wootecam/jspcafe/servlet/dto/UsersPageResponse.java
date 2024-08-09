package com.wootecam.jspcafe.servlet.dto;

import com.wootecam.jspcafe.domain.User;
import java.util.List;

public class UsersPageResponse {

    private final int userCount;
    private final int currentPage;
    private final List<UserResponse> userResponses;

    public UsersPageResponse(final int userCount, final int currentPage,
                             final List<UserResponse> userResponses) {
        this.userCount = userCount;
        this.currentPage = currentPage;
        this.userResponses = userResponses;
    }

    public static UsersPageResponse of(final int userCount, final int currentPage,
                                       final List<User> users) {
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::from)
                .toList();

        return new UsersPageResponse(
                userCount,
                currentPage,
                userResponses
        );
    }

    public int getUserCount() {
        return userCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<UserResponse> getUserResponses() {
        return userResponses;
    }
}
