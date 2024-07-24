package org.example.jspcafe.user.request;

public record LoginServiceRequest (
        String email,
        String password
) {
}
