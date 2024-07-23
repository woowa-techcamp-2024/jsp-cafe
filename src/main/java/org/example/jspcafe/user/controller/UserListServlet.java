package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.user.response.UserListResponse;
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;

@WebServlet(name = "userListServlet", value = "/users")
public class UserListServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = ApplicationContext.getContainer().resolve(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final UserListResponse userList = userService.getUserList();

        req.setAttribute("userList", userList);
        req.getRequestDispatcher("/userlist.jsp").forward(req, resp);
    }
}
