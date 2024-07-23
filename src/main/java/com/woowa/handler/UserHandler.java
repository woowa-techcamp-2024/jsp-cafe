package com.woowa.handler;

import com.woowa.database.UserDatabase;
import com.woowa.framework.web.HttpMethod;
import com.woowa.framework.web.RequestMapping;
import com.woowa.framework.web.RequestParameter;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UserHandler {

    private final UserDatabase userDatabase;

    public UserHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @RequestMapping(path = "/users", method = HttpMethod.POST)
    public ResponseEntity createUser(
            @RequestParameter("email") String email,
            @RequestParameter("nickname") String nickname,
            @RequestParameter("password") String password) {
        userDatabase.findByEmail(email)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("사용할 수 없는 이메일입니다.");
                });

        User user = User.create(UUID.randomUUID().toString(), email, password, nickname);
        userDatabase.save(user);
        return ResponseEntity.builder().found("/users");
    }

    @RequestMapping(path = "/users", method = HttpMethod.GET)
    public ResponseEntity listUsers(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("users", userDatabase.findAll());
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/classes/static/user/list.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException("대상 리소스를 찾지 못하였습니다.");
        }
        return null;
    }
}
