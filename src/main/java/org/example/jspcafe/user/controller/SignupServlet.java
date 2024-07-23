package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;
import org.example.jspcafe.user.service.SignupService;

import java.io.IOException;

@WebServlet(name = "signupServlet", value = "/api/signup")
public class SignupServlet extends HttpServlet {

    private SignupService signupService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.signupService = ApplicationContext.getContainer().resolve(SignupService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nickname = req.getParameter("nickname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        final RegisterUserServiceRequest request = new RegisterUserServiceRequest(nickname, email, password);

        try {
            signupService.registerUser(request);
            resp.sendRedirect("/login");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/signup/index.jsp").forward(req, resp);

        }
    }
}
