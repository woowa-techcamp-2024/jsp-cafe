package org.example.util.session;

import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemorySessionManager implements SessionManager {

    private static final class InstanceHolder {
        private static final SessionManager INSTANCE = new InMemorySessionManager();
    }

    public static SessionManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final Map<String, HttpSession> sessions = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Duration sessionTimeout = Duration.ofMinutes(30);

    private InMemorySessionManager() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::cleanExpiredSessions, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public HttpSession getSession(String sessionId) {
        lock.readLock().lock();
        try {
            HttpSession session = sessions.get(sessionId);
            if (session != null && session instanceof Session) {
                ((Session) session).access();
            }
            return session;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public HttpSession createSession() {
        HttpSession session = new Session();
        lock.writeLock().lock();
        try {
            sessions.put(session.getId(), session);
        } finally {
            lock.writeLock().unlock();
        }
        return session;
    }

    @Override
    public void invalidateSession(String sessionId) {
        lock.writeLock().lock();
        try {
            HttpSession session = sessions.remove(sessionId);
            if (session != null) {
                session.invalidate();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void cleanExpiredSessions() {
        Instant now = Instant.now();
        sessions.entrySet().removeIf(entry -> {
            HttpSession session = entry.getValue();
            if (session instanceof Session) {
                Session customSession = (Session) session;
                Duration sessionAge = Duration.between(customSession.getLastAccess(), now);
                return sessionAge.compareTo(sessionTimeout) > 0;
            }
            return false;
        });
    }
}