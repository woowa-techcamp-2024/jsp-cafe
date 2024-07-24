package org.example.jspcafe.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;

@WebServlet(name = "editProfileServlet", value = "/api/edit-profile")
public class EditProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = ApplicationContext.getContainer().resolve(UserService.class);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        // 변경할 필드를 담기 위한 변수
        String newNickname = req.getParameter("nickname");
        String newPassword = req.getParameter("password");

        if(newNickname == null || newNickname.isEmpty()) {
            req.setAttribute("errorMessage", "변경할 정보가 없습니다.");
            req.getRequestDispatcher("/edit-profile.jsp").forward(req, resp);
            return;
        }
        if(newPassword == null || newPassword.isEmpty()) {
            req.setAttribute("errorMessage", "비밀번호가 비어있습니다.");
            req.getRequestDispatcher("/edit-profile.jsp").forward(req, resp);
            return;
        }

        try {
            userService.editProfile(userId, newNickname, newPassword);
            req.getSession().setAttribute("nickname", newNickname);
            resp.sendRedirect("/");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/edit-profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getParameter("_method"))) {
            doPatch(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
