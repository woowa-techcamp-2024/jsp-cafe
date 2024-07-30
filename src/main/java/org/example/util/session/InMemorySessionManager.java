package org.example.util.session;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.service.UserService;

@Component
public class InMemorySessionManager implements SessionManager {
    private final Map<String, InternalSession> sessions = new HashMap<>();
    private final UserService userService;

    @Autowired
    public InMemorySessionManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpSession addSessionToManager(HttpSession session) throws SQLException {
        String userId = (String) session.getAttribute("userId");
        UserResponseDto user = userService.getUserFromUserId(userId);

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
    public UserResponseDto getUserDetails(String sessionId) {
        InternalSession internalSession = sessions.get(sessionId);
        if (internalSession != null) {
            return (UserResponseDto) internalSession.getAttribute("userDetails");
        }
        return null;
    }

    // 내부용 세션 클래스
    private static class InternalSession {
        private final HttpSession originalSession;
        private final Map<String, Object> internalAttributes = new HashMap<>();

        InternalSession(HttpSession session) {
            this.originalSession = session;
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
    }


}