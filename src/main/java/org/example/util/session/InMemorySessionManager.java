package org.example.util.session;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dto.UserDto;
import org.example.member.service.UserService;

@Component
public class InMemorySessionManager implements SessionManager {
    private final Map<String, InternalSession> sessions = new HashMap<>();
    private final UserService userService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public InMemorySessionManager(UserService userService) {
        this.userService = userService;
        startCleanupTask();
    }

    @Override
    public HttpSession addSessionToManager(HttpSession session) throws SQLException {
        String userId = (String) session.getAttribute("userId");
        UserDto user = userService.getUserFromUserId(userId);

        // 내부용 세션 객체 생성
        InternalSession internalSession = new InternalSession(session);
        internalSession.setAttribute("userDetails", user);

        sessions.put(session.getId(), internalSession);
        return session;
    }

    @Override
    public HttpSession getSessionFromManager(String sessionId) {
        InternalSession internalSession = sessions.get(sessionId);
        return internalSession != null ? internalSession.getOriginalSession() : null;
    }

    @Override
    public void invalidateSession(String sessionId) {
        InternalSession internalSession = sessions.get(sessionId);
        if (internalSession != null) {
            internalSession.getOriginalSession().invalidate();
        }
        sessions.remove(sessionId);
    }

    @Override
    public UserDto getUserDetails(String sessionId) {
        InternalSession internalSession = sessions.get(sessionId);
        if (internalSession != null) {
            return (UserDto) internalSession.getAttribute("userDetails");
        }
        return null;
    }

    @Override
    public void updateSessionUserInfo(HttpSession session, UserDto userDto) {
        InternalSession internalSession = sessions.get(session.getId());
        if (internalSession != null) {
            internalSession.setAttribute("userDetails", userDto);
        }
    }

    private void startCleanupTask() {
        scheduler.scheduleAtFixedRate(this::cleanExpiredSessions, 1, 1, TimeUnit.MINUTES);
    }

    public void cleanExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> isSessionExpired(entry.getValue(), now));
    }

    private boolean isSessionExpired(InternalSession session, long now) {
        return now - session.getLastAccessTime() > session.getOriginalSession().getMaxInactiveInterval() * 1000;
    }

    @Override
    public String toString() {
        return "InMemorySessionManager{" +
                "sessions=" + sessions +
                ", userService=" + userService +
                ", scheduler=" + scheduler +
                '}';
    }

    // 내부용 세션 클래스
    private static class InternalSession {
        private final HttpSession originalSession;
        private final Map<String, Object> internalAttributes = new HashMap<>();
        private long lastAccessTime;

        InternalSession(HttpSession session) {
            this.originalSession = session;
            this.lastAccessTime = System.currentTimeMillis();
        }

        void setAttribute(String name, Object value) {
            internalAttributes.put(name, value);
        }

        Object getAttribute(String name) {
            return internalAttributes.get(name);
        }

        HttpSession getOriginalSession() {
            return originalSession;
        }

        void updateLastAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }

        public Map<String, Object> getInternalAttributes() {
            return internalAttributes;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        @Override
        public String toString() {
            return "InternalSession{" +
                    "originalSession=" + originalSession +
                    ", internalAttributes=" + internalAttributes +
                    ", lastAccessTime=" + lastAccessTime +
                    '}';
        }
    }
}