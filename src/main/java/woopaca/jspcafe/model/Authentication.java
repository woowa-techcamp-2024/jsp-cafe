package woopaca.jspcafe.model;

import java.time.LocalDateTime;

public class Authentication {

    private final User principal;
    private final LocalDateTime authenticatedAt;

    public Authentication(User principal, LocalDateTime authenticatedAt) {
        this.principal = principal;
        this.authenticatedAt = authenticatedAt;
    }
}
