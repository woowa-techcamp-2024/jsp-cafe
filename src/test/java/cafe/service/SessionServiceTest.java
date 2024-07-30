package cafe.service;

import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionServiceTest {
    private static UserDatabase userDatabase;
    private static SessionDatabase sessionDatabase;
    private static SessionService sessionService;

    @BeforeAll
    static void setUp() {
        userDatabase = mock(UserDatabase.class);
        sessionDatabase = new SessionDatabase();
        sessionService = new SessionService(userDatabase, sessionDatabase);
    }

    @AfterEach
    void tearDown() {
        sessionDatabase.deleteAll();
    }

    @Test
    void 올바른_로그인_정보를_저장한다() {
        // given
        String sessionid = "sessionid";
        String userid = "userid";
        String password = "password";
        when(userDatabase.selectById(userid)).thenReturn(User.of(userid, "name", password, "email@email"));

        // when
        sessionService.signIn(sessionid, userid, password);

        // then
        assertTrue(sessionService.isSignIn(sessionid));
    }

    @Test
    void 올바르지_않은_로그인_정보는_예외가_발생한다() {
        // given
        String sessionid = "sessionid";
        String userid = "userid";
        String password = "password";

        // when, then
        assertThrows(IllegalArgumentException.class, () -> sessionService.signIn(sessionid, userid, password));
    }

    @Test
    void 로그아웃_시_세션_정보를_삭제한다() {
        // given
        String sessionid = "sessionid";
        String userid = "userid";
        String password = "password";
        when(userDatabase.selectById(userid)).thenReturn(User.of(userid, "name", password, "email@email"));
        sessionService.signIn(sessionid, userid, password);

        // when
        sessionService.signOut(sessionid);

        // then
        assertFalse(sessionService.isSignIn(sessionid));
    }

    @Test
    void 로그인_정보를_조회한다() {
        // given
        String sessionid = "sessionid";
        String userid = "userid";
        String password = "password";
        when(userDatabase.selectById(userid)).thenReturn(User.of(userid, "name", password, "email@email"));
        sessionService.signIn(sessionid, userid, password);

        // when
        boolean isSignIn = sessionService.isSignIn(sessionid);

        // then
        assertTrue(isSignIn);
    }

    @Test
    void 세션_아이디에_해당하는_유저를_조회한다() {
        // given
        String sessionid = "sessionid";
        String userid = "userid";
        String password = "password";
        when(userDatabase.selectById(userid)).thenReturn(User.of(userid, "name", password, "email@email"));
        sessionService.signIn(sessionid, userid, password);

        // when
        User user = sessionService.findUserBySession(sessionid);

        // then
        assertEquals(userid, user.getUserId());
    }

    @Test
    void 세션_아이디가_없으면_예외가_발생한다() {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> sessionService.findUserBySession("wrongSessionId"));
    }
}