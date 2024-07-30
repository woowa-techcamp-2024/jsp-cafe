package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.User;
import com.codesquad.cafe.model.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServlet extends UserServlet {

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
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = userRepository.findByUsername(username).get();

            if (!user.getPassword().equals(password)) {
                handleLoginFail(req, resp);
                return;
            }
            handleLoginSuccess(req, resp, user);
        } catch (NoSuchElementException | NullPointerException exception) {
            handleLoginFail(req, resp);
        }
    }

    private void handleLoginFail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("error", "로그인 실패");
        req.getRequestDispatcher(LOGIN_FORM_JSP).forward(req, resp);
    }

    private void handleLoginSuccess(HttpServletRequest req, HttpServletResponse resp, User authenticatedUser)
            throws IOException {
        HttpSession session = req.getSession(true);
        session.setAttribute(SESSION_USER_PRINCIPAL_KEY,
                new UserPrincipal(authenticatedUser.getId(), authenticatedUser.getEmail()));

        resp.sendRedirect("/");

    }

}
