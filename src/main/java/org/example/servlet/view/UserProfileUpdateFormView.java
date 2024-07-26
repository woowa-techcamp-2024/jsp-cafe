package org.example.servlet.view;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = {"/users/update-form/*"})
public class UserProfileUpdateFormView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserProfileUpdateFormView.class);

    private UserDataHandler userDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDataHandler = (UserDataHandler) config.getServletContext().getAttribute(DataHandler.USER.getValue());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("[UserProfileUpdateFormView] called");
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.valueOf(pathParts[pathInfo.length() - 1]);
        log.debug("[UserProfileUpdateFormView] " + userId);
        User user = userDataHandler.findByUserId(userId);
        if (user == null) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "수정하려는 회원이 없습니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile-update-form.jsp").forward(request, response);
    }
}
