package org.example.util.session;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private final String id;
    private final Map<String, Object> attributes = new HashMap<>();
    private Instant lastAccess;
    private final Instant creationTime;

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.lastAccess = Instant.now();
        this.creationTime = Instant.now();
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Instant getLastAccess() {
        return lastAccess;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setLastAccess(Instant lastAccess) {
        this.lastAccess = lastAccess;
    }

    public void setAttributes(String name, Object value) {
        this.attributes.put(name, value);
    }

    public void access() {
        this.lastAccess = Instant.now();
    }
}
