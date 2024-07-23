package com.jspcafe.user.controller;

import com.jspcafe.user.model.User;
import com.jspcafe.user.service.UserService;
import com.jspcafe.util.HttpUtils;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/users/*")
public class UserController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        userService = (UserService) ctx.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty() || path.isBlank()) {
            userList(req, resp);
            return;
        }
        if (path.endsWith("/form")) {
            updateProfileForm(req, resp);
            return;
        }
        if (path.endsWith("/form-failed")) {
            updateProfileFormFailed(req, resp);
            return;
        }
        switch (path) {
            case "/sign" -> forward("signup", req, resp);
            default -> userProfile(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        signUp(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateProfile(req, resp);
    }

    private void forward(String fileName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/" + fileName + ".jsp").forward(req, resp);
    }

    private void signUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        userService.signUp(email, nickname, password);
        resp.sendRedirect("/users");
    }

    private void userList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.findAll();
        req.setAttribute("users", users);
        forward("user_list", req, resp);
    }

    private void userProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().replace("/", "");
        User user = userService.findById(id);
        req.setAttribute("user", user);
        forward("user_profile", req, resp);
    }

    private void updateProfileForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().replace("/form", "");
        String id = path.replace("/", "");
        User user = userService.findById(id);
        req.setAttribute("user", user);
        forward("user_update_form", req, resp);
    }

    private void updateProfileFormFailed(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo().replace("/form-failed", "");
        String id = path.replace("/", "");
        User user = userService.findById(id);
        req.setAttribute("user", user);
        forward("user_update_form_failed", req, resp);
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().replace("/", "");
        Map<String, Object> data = HttpUtils.getJsonRequestBody(req);
        String currentPassword = (String) data.get("currentPassword");
        User user = userService.findById(id);
        if (!user.verifyPassword(currentPassword)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
            resp.setHeader("Location", "/users/" + id + "/form-failed");
            return;
        }
        String email = (String) data.get("email");
        String nickname = (String) data.get("nickname");
        String newPassword = (String) data.get("newPassword");
        userService.update(user, email, nickname, newPassword);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER); // 303 상태 코드
        resp.setHeader("Location", "/users/" + id);
    }
}
