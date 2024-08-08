package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.dao.UserRepository;
import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.model.dto.ErrorResponse;
import com.codesquad.cafe.model.dto.RedirectResponse;
import com.codesquad.cafe.model.dto.UserEditRequest;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.JsonModelMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMeServlet extends HttpServlet {

    private static final String USER_EDIT_FORM_JSP = "/WEB-INF/views/user_edit_form.jsp";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = getUserPrincipal(req);
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(ValidationException::new);

        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/views/user_profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = getUserPrincipal(req);

        UserEditRequest requestDto = JsonModelMapper.map(req, UserEditRequest.class);
        requestDto.validate();

        if (userPrincipal == null || !userPrincipal.getId().equals(requestDto.getId())) {
            throw new AuthorizationException();
        }

        Optional<User> user = userRepository.findById(requestDto.getId());
        if (user.isEmpty() || user.get().isDeleted()) {
            handleValidateFail(req, resp, "이미 탈퇴한 유저입니다.");
        }

        User originalUser = user.get();

        if (!requestDto.getOriginalPassword().equals(originalUser.getPassword())) {
            handleValidateFail(req, resp, "기존 비밀번호가 일치하지 않습니다.");
            return;
        }
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            handleValidateFail(req, resp, "비밀번호가 비밀번호 확인과 일치하지 않습니다.");
            return;
        }

        if (!originalUser.getUsername().equals(requestDto.getUsername())) {
            handleValidateFail(req, resp, "사용자 아이디는 변경할 수 없습니다.");
            return;
        }

        originalUser.update(requestDto.getPassword(), requestDto.getName(), requestDto.getEmail());
        userRepository.save(originalUser);

        JsonModelMapper.json(resp, new RedirectResponse("/me"));
    }

    private void handleValidateFail(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        resp.setStatus(400);
        JsonModelMapper.json(resp, new ErrorResponse(message));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = getUserPrincipal(req);

        Optional<User> user = userRepository.findById(userPrincipal.getId());

        // 존재하지 않는 유저이거나 이미 탈퇴한 유저의 경우
        if (user.isEmpty() || user.get().isDeleted()) {
            throw new ValidationException();
        }

        // 유저 soft delete
        User originalUser = user.get();
        originalUser.delete();
        userRepository.save(originalUser);

        //session invalidate
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        JsonModelMapper.json(resp, new RedirectResponse("/users"));
    }
}
