package woopaca.jspcafe.model;

import java.time.LocalDateTime;

public record Authentication(User principal, LocalDateTime authenticatedAt) {

    public boolean isPrincipal(Long userId) {
        return principal.getId().equals(userId);
    }

    public Authentication updatePrincipal(User user) {
        return new Authentication(user, authenticatedAt);
    }
}
