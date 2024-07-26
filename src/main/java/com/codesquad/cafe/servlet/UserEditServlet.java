package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.exception.ModelMappingException;
import com.codesquad.cafe.model.User;
import com.codesquad.cafe.model.UserEditRequest;
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

    private static final Logger log = LoggerFactory.getLogger(UserEditServlet.class);

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
            String[] split = req.getQueryString().split("=");
            if (split.length != 2 || !split[0].equals("id")) {
                resp.sendError(400);
                return;
            }

            Long id = Long.parseLong(split[1]);
            Optional<User> user = userRepository.findById(id);
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
            UserEditRequest requestDto = RequestParamModelMapper.map(req.getParameterMap(), UserEditRequest.class);

            Optional<User> user = userRepository.findById(requestDto.getId());
            if (user.isEmpty() || user.get().isDeleted()) {
                resp.sendError(400);
                return;
            }

            if (requestDto.getPassword() == null ||
                    requestDto.getPassword().isBlank() ||
                    !requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
                req.setAttribute("user", user.get());
                req.setAttribute("error", "비밀번호가 일치하지 않습니다.");
                req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
                return;
            }

            User originalUser = user.get();
            if (!originalUser.getUsername().equals(requestDto.getUsername())) {
                req.setAttribute("user", user.get());
                req.setAttribute("error", "유저 이름은 변경할 수 없습니다.");
                req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
                return;
            }
            originalUser.update(requestDto.getPassword(), requestDto.getName(), requestDto.getEmail());
            userRepository.save(originalUser);
            resp.sendRedirect("/users/" + user.get().getId());
        } catch (ModelMappingException | IllegalArgumentException e) {
            resp.sendError(400);
        }

    }
}
