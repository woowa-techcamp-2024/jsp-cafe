package org.example.jspcafe.user.response;

import java.util.List;

public record UserListResponse(
        int count,
        List<UserResponse> userList
) {
    public static UserListResponse of(List<UserResponse> userList) {
        return new UserListResponse(userList.size(), userList);
    }
}
