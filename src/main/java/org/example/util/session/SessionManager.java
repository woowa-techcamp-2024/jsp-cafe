package org.example.util.session;

import jakarta.servlet.http.HttpSession;

public interface SessionManager {

    HttpSession getSession(String sessionId);
    HttpSession addSessionToManager(HttpSession session);
    void invalidateSession(String sessionId);
}
