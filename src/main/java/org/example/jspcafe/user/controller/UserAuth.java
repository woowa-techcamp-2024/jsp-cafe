package org.example.jspcafe.user.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.MemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.service.AuthService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AuthController", value = "/users")
public class UserAuth extends HttpServlet {

    private AuthService authService;
    private UserRepository userRepository;

    public void init(){
        this.userRepository = new MemoryUserRepository();
        this.authService = new AuthService(userRepository);
    }

    public void destroy() {

    }

    public UserAuth() {
        this.userRepository = new MemoryUserRepository();
        this.authService = new AuthService(userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do Get 실행");
        List<User> all = userRepository.getAll();
        req.setAttribute("users", all);
        req.getRequestDispatcher("/user/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String userId = req.getParameter("userId");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        User user = new User(userId,password,nickname,email);
        authService.register(user);
        resp.sendRedirect("users");
    }
}
