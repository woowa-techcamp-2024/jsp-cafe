package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.User;
import com.codesquad.cafe.exception.ModelMappingException;
import com.codesquad.cafe.model.UserEditRequest;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEditServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_EDIT_FORM_JSP = "/WEB-INF/views/user_edit_form.jsp";

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute(LoginServlet.SESSION_USER_PRINCIPAL_KEY) == null) {
                resp.sendError(401);
                return;
            }

            UserPrincipal userPrincipal = ((UserPrincipal) session.getAttribute(
                    LoginServlet.SESSION_USER_PRINCIPAL_KEY));
            if (userPrincipal == null) {
                resp.sendError(401);
                return;
            }

            Optional<User> user = userRepository.findById(userPrincipal.getId());
            if (user.isEmpty() || user.get().isDeleted()) {
                resp.sendError(400);
                return;
            }

            req.setAttribute("user", user.get());
            req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute(LoginServlet.SESSION_USER_PRINCIPAL_KEY) == null) {
                resp.sendError(401);
                return;
            }

            UserPrincipal userPrincipal = ((UserPrincipal) session.getAttribute(
                    LoginServlet.SESSION_USER_PRINCIPAL_KEY));

            UserEditRequest requestDto = RequestParamModelMapper.map(req.getParameterMap(), UserEditRequest.class);
            if (!requestDto.isValid()) {
                resp.sendError(400);
                return;
            }

            if (userPrincipal == null || !userPrincipal.getId().equals(requestDto.getId())) {
                resp.sendError(401);
                return;
            }

            Optional<User> user = userRepository.findById(requestDto.getId());
            if (user.isEmpty() || user.get().isDeleted()) {
                resp.sendError(400);
                return;
            }

            User originalUser = user.get();

            if (!requestDto.getOriginalPassword().equals(originalUser.getPassword())) {
                handleValidateFail(req, resp, originalUser, "기존 비밀번호가 일치하지 않습니다.");
                return;
            }
            if (requestDto.getPassword() == null ||
                    requestDto.getPassword().isBlank() ||
                    !requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
                handleValidateFail(req, resp, originalUser, "비밀번호가 비밀번호 확인과 일치하지 않습니다.");
                return;
            }

            if (!originalUser.getUsername().equals(requestDto.getUsername())) {
                handleValidateFail(req, resp, originalUser, "유저 이름은 변경할 수 없습니다.");
                return;
            }

            originalUser.update(requestDto.getPassword(), requestDto.getName(), requestDto.getEmail());
            userRepository.save(originalUser);
            resp.sendRedirect("/users/" + user.get().getId());
        } catch (ModelMappingException | IllegalArgumentException e) {
            resp.sendError(500);
        }
    }

    private void handleValidateFail(HttpServletRequest req, HttpServletResponse resp, User user, String message)
            throws ServletException, IOException {
        req.setAttribute("user", user);
        req.setAttribute("error", message);
        req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
    }

}
