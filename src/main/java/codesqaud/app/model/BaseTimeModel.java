package codesqaud.app.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public abstract class BaseTimeModel {
    protected OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
