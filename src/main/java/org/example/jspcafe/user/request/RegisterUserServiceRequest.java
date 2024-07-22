package org.example.jspcafe.user.request;

public record RegisterUserServiceRequest (
    String nickname,
    String email,
    String password
) {
}
