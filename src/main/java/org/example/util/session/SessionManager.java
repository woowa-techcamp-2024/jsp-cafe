package org.example.util.session;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import org.example.member.model.dto.UserResponseDto;

public interface SessionManager {

    HttpSession getSessionFromManager(String sessionId);
    HttpSession addSessionToManager(HttpSession session) throws SQLException;
    void invalidateSession(String sessionId);
    UserResponseDto getUserDetails(String sessionId);
}
