package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.payload.request.UserLoginRequest;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/login")
public class UserLoginServlet extends HttpServlet {

    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        // TODO: verify Session
        // 필터 단에서 요청에 대한 세션이 존재하면 넘기는거로?
        req.getRequestDispatcher("/WEB-INF/jsp/userLogin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        UserSessionResponse sessionResponse = userService.loginUser(
            UserLoginRequest.from(req.getParameterMap()));
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        req.getSession(true).setAttribute("user", sessionResponse);
        resp.sendRedirect("/index.html");
    }
}
