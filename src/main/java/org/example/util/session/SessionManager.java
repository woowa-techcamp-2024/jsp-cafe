package org.example.util.session;

import jakarta.servlet.http.HttpSession;

public interface SessionManager {

    HttpSession getSession(String sessionId);
    HttpSession createSession();
    void invalidateSession(String sessionId);
}
