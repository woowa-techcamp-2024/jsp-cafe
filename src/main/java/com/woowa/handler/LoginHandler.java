package com.woowa.handler;

import com.woowa.database.UserDatabase;
import com.woowa.framework.web.HttpMethod;
import com.woowa.framework.web.RequestMapping;
import com.woowa.framework.web.RequestParameter;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;

public class LoginHandler {

    private final UserDatabase userDatabase;

    public LoginHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @RequestMapping(path = "/login", method = HttpMethod.POST)
    public ResponseEntity login(
            @RequestParameter("email") String email,
            @RequestParameter("password") String password,
            HttpServletRequest request) {
        User user = userDatabase.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("아이디/비밀번호가 일치하지 않습니다."));
        user.checkPassword(password);
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getUserId());
        return ResponseEntity.builder()
                .add("userId", user.getUserId())
                .found("/");
    }

    @RequestMapping(path = "/logout", method = HttpMethod.POST)
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return ResponseEntity.builder()
                .found("/");
    }
}
