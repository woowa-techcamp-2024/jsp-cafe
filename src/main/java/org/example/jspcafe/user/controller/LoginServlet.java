package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.ApplicationContext;
import org.example.jspcafe.user.request.LoginServiceRequest;
import org.example.jspcafe.user.service.LoginService;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/api/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.loginService = ApplicationContext.getContainer().resolve(LoginService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        LoginServiceRequest request = new LoginServiceRequest(email, password);
        try {
            loginService.login(request);
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/login/index.jsp").forward(req, resp);
        }
    }

}
