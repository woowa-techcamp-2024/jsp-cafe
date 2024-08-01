package org.example.util.session;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import org.example.member.model.dto.UserDto;
import org.example.member.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InMemorySessionManagerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private InMemorySessionManager sessionManager;

    @Test
    @DisplayName("유효한 세션이 주어졌을 때, 세션 매니저는 해당 세션을 정상적으로 저장해야한다.")
    public void test_add_session_to_manager_stores_session_correctly() throws SQLException {
        // Given
        when(session.getId()).thenReturn("session1");
        when(session.getAttribute("userId")).thenReturn("user1");
        UserDto userDto = new UserDto();
        when(userService.getUserFromUserId("user1")).thenReturn(userDto);

        // When
        sessionManager.addSessionToManager(session);

        // Then
        assertNotNull(sessionManager.getSessionFromManager("session1"));
    }

    @Test
    @DisplayName("세션 id가 주어졌을 때 저장 돼 있는 세션이라면 정상적으로 세션을 가져올 수 있다.")
    public void test_retrieve_session_by_id_returns_correct_session() throws SQLException {
        // Given
        when(session.getId()).thenReturn("session1");
        when(session.getAttribute("userId")).thenReturn("user1");
        UserDto userDto = new UserDto();
        when(userService.getUserFromUserId("user1")).thenReturn(userDto);
        sessionManager.addSessionToManager(session);

        // When
        HttpSession retrievedSession = sessionManager.getSessionFromManager("session1");

        // Then
        assertEquals(session, retrievedSession);
    }

    @Test
    @DisplayName("유효한 세션ID를 통해 invalidate를 하면 해당 세션이 삭제된다.")
    public void test_invalidate_session_removes_it_from_manager() throws SQLException {
        // Given
        when(session.getId()).thenReturn("session1");
        when(session.getAttribute("userId")).thenReturn("user1");
        UserDto userDto = new UserDto();
        when(userService.getUserFromUserId("user1")).thenReturn(userDto);
        sessionManager.addSessionToManager(session);

        // When
        sessionManager.invalidateSession("session1");

        // Then
        assertNull(sessionManager.getSessionFromManager("session1"));
    }

    @Test
    @DisplayName("유효한 세션이 주어졌을 때, getUserDetails은 올바른 사용자 정보를 반환한다.")
    public void test_get_user_details_returns_correct_userdto() throws SQLException {
        // Given
        when(session.getId()).thenReturn("session1");
        when(session.getAttribute("userId")).thenReturn("user1");
        UserDto userDto = new UserDto();
        when(userService.getUserFromUserId("user1")).thenReturn(userDto);
        sessionManager.addSessionToManager(session);

        // When
        UserDto retrievedUserDto = sessionManager.getUserDetails("session1");

        // Then
        assertEquals(userDto, retrievedUserDto);
    }

    @Test
    @DisplayName("올바른 세션과 사용자 정보가 주어졌을 때 사용자 정보가 업데이트 되면 세션에 저장된 정보도 업데이트 된다.")
    public void test_update_session_user_info_updates_user_details() throws SQLException {
        // Given
        when(session.getId()).thenReturn("session1");
        when(session.getAttribute("userId")).thenReturn("user1");
        UserDto userDto = new UserDto();
        when(userService.getUserFromUserId("user1")).thenReturn(userDto);
        sessionManager.addSessionToManager(session);

        // When
        UserDto newUserDto = new UserDto();
        newUserDto.setName("New Name");
        sessionManager.updateSessionUserInfo(session, newUserDto);

        // Then
        assertEquals(newUserDto, sessionManager.getUserDetails("session1"));
    }

    @Test
    @DisplayName("세션에 저장된 userId가 null일 때 세션 매니저에 저장하려하면 IllegalArgumentException을 발생시킨다.")
    public void test_add_session_with_null_userid_handles_gracefully() throws SQLException {
        // Given
        when(session.getAttribute("userId")).thenReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            sessionManager.addSessionToManager(session);
        });
    }

    @Test
    @DisplayName("존재하지 않은 세션ID로 조회시 null을 반환한다.")
    public void test_retrieve_non_existent_session_returns_null() {
        // When
        HttpSession retrievedSession = sessionManager.getSessionFromManager("nonExistent");

        // Then
        assertNull(retrievedSession);
    }

    @Test
    @DisplayName("존재하지 않는 sessionId로 세션을 invalidate 하려고 하면 아무 일도 멀어지지 않는다.")
    public void test_invalidate_non_existent_session_handles_gracefully() {
        // When & Then
        assertDoesNotThrow(() -> {
            sessionManager.invalidateSession("nonExistent");
        });
    }
}