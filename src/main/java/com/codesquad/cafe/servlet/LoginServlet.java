package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.StringUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServlet extends HttpServlet {

    public static final String SESSION_USER_PRINCIPAL_KEY = "userPrincipal";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String LOGIN_FORM_JSP = "/WEB-INF/views/login_form.jsp";

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_FORM_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
            handleLoginFail(req, resp, "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
            return;
        }

        Optional<User> user = userRepository.findByUsername(username);

        if (user == null || !user.get().getPassword().equals(password)) {
            handleLoginFail(req, resp, "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.");
            return;
        }

        if (user.get().isDeleted()) {
            handleLoginFail(req, resp, "탈퇴한 유저입니다.");
            return;
        }
        handleLoginSuccess(req, resp, user.get());
    }

    private void handleLoginFail(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher(LOGIN_FORM_JSP).forward(req, resp);
    }

    private void handleLoginSuccess(HttpServletRequest req, HttpServletResponse resp, User authenticatedUser)
            throws IOException {
        HttpSession session = req.getSession(true);
        session.setAttribute(SESSION_USER_PRINCIPAL_KEY,
                new UserPrincipal(authenticatedUser.getId(), authenticatedUser.getUsername()));

        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        if (redirectUrl != null) {
            session.removeAttribute("redirectAfterLogin");
            resp.sendRedirect(redirectUrl);
            return;
        }

        resp.sendRedirect("/");
    }

}
