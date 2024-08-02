package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.util.StringUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEditFormServlet extends HttpServlet {

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
        UserPrincipal userPrincipal = getUserPrincipal(req);
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        if (user.isEmpty() || user.get().isDeleted()) {
            throw new ResourceNotFoundException();
        }

        String error = req.getParameter("error");
        req.setAttribute("user", user.get());
        if (!StringUtil.isBlank(error)) {
            req.setAttribute("error", error);
        }
        req.getRequestDispatcher(USER_EDIT_FORM_JSP).forward(req, resp);
    }
}
