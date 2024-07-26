package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = parsePathVariable(req.getPathInfo());
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                resp.sendError(404);
                return;
            }
            req.setAttribute("user", user.get());

            req.getRequestDispatcher("/WEB-INF/views/user_profile.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if (method != null && method.toLowerCase(Locale.ROOT).equals("delete")) {
            doDelete(req, resp);
            return;
        }
        resp.sendError(405);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.valueOf(req.getParameter("id"));
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty() ||user.get().isDeleted()){
                resp.sendError(400);
            }
            User originalUser = user.get();
            originalUser.delete();
            userRepository.save(originalUser);
            resp.sendRedirect("/users");
        } catch (NumberFormatException e) {
            resp.sendError(404);
        }
    }

    private Long parsePathVariable(String pathInfo) {

        String[] paths = pathInfo.substring(1).split("/");
        return Long.parseLong(paths[0]);
    }
}
