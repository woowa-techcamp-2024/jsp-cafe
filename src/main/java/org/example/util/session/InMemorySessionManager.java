package org.example.util.session;

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

    private final Map<String, Session> sessions = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Duration sessionTimeout = Duration.ofMinutes(30);

    private InMemorySessionManager() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::cleanExpiredSessions, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public Session getSession(String sessionId) {
        lock.readLock().lock();
        try {
            Session session = sessions.get(sessionId);
            if (session != null) {
                session.access();
            }
            return session;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Session createSession(String sessionId) {
        Session session = new Session();
        lock.writeLock().lock();
        try {
            sessions.put(sessionId, session);
        } finally {
            lock.writeLock().unlock();
        }
        return session;
    }

    @Override
    public void invalidateSession(String sessionId) {
        lock.writeLock().lock();
        try {
            sessions.remove(sessionId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void cleanExpiredSessions() {
        Instant now = Instant.now();
        sessions.entrySet().removeIf(entry -> {
            Session session = entry.getValue();
            Duration sessionAge = Duration.between(session.getLastAccess(), now);
            return sessionAge.compareTo(sessionTimeout) > 0;
        });
    }
}
