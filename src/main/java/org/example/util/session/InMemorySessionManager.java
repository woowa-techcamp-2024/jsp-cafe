package org.example.util.session;

import jakarta.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemorySessionManager implements SessionManager {

    private final ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<>();

    private static final class InstanceHolder {
        private static final SessionManager INSTANCE = new InMemorySessionManager();
    }

    public static SessionManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private InMemorySessionManager() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::cleanInactiveSessions, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public HttpSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public HttpSession addSessionToManager(HttpSession session) {
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void invalidateSession(String sessionId) {
        HttpSession session = sessions.remove(sessionId);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // 세션이 이미 무효화된 경우 무시
            }
        }
    }

    private void cleanInactiveSessions() {
        sessions.entrySet().removeIf(entry -> {
            HttpSession session = entry.getValue();
            try {
                // 세션에 접근을 시도하여 유효한지 확인
                session.getLastAccessedTime();
                return false; // 세션이 유효함
            } catch (IllegalStateException e) {
                // 세션이 이미 무효화된 경우
                return true; // 맵에서 제거
            }
        });
    }
}