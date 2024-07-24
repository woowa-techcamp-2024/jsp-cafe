package org.example.util.session;

public interface SessionManager {

    Session getSession(String sessionId);
    Session createSession(String sessionId);
    void invalidateSession(String sessionId);
}
