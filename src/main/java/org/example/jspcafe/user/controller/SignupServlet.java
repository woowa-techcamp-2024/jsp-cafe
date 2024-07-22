package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;
import org.example.jspcafe.user.service.SignupService;

import java.io.IOException;

@WebServlet(name = "signupServlet", value = "/signup")
public class SignupServlet extends HttpServlet {

    private final SignupService signupService;

    // TODO 의존성 주입할 방법 찾기
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        final RegisterUserServiceRequest request = new RegisterUserServiceRequest(nickname, email, password);

        signupService.registerUser(request);


        resp.sendRedirect("/login");
    }

    public SignupServlet(SignupService signupService) {
        this.signupService = signupService;
    }
}
