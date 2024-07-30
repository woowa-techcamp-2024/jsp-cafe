package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.User;
import com.codesquad.cafe.model.UserEditRequest;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.RequestParamModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        Optional<User> user = userRepository.findById(getUserPrincipal(req).getId());
        if (user.isEmpty() || user.get().isDeleted()) {
            resp.sendError(400);
            return;
        }

        req.setAttribute("user", user.get());
        req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = getUserPrincipal(req);

        UserEditRequest requestDto;
        requestDto = RequestParamModelMapper.map(req.getParameterMap(), UserEditRequest.class);
        requestDto.validate();

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
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            handleValidateFail(req, resp, originalUser, "비밀번호가 비밀번호 확인과 일치하지 않습니다.");
            return;
        }

        if (!originalUser.getUsername().equals(requestDto.getUsername())) {
            handleValidateFail(req, resp, originalUser, "사용자 아이디는 변경할 수 없습니다.");
            return;
        }

        originalUser.update(requestDto.getPassword(), requestDto.getName(), requestDto.getEmail());
        userRepository.save(originalUser);
        resp.sendRedirect("/users/" + user.get().getId());
    }

    private void handleValidateFail(HttpServletRequest req, HttpServletResponse resp, User user, String message)
            throws ServletException, IOException {
        req.setAttribute("user", user);
        req.setAttribute("error", message);
        req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
    }

}
