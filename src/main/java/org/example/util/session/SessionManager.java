package org.example.util.session;

import jakarta.servlet.http.HttpSession;

public interface SessionManager {

    HttpSession getSession(String sessionId);
    HttpSession createSession(String sessionId, HttpSession session);
    void invalidateSession(String sessionId);
}
