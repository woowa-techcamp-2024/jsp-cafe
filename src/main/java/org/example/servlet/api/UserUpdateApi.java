package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.constance.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/users/update/*")
public class UserUpdateApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserUpdateApi.class);
    private UserDataHandler userDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDataHandler = (UserDataHandler) config.getServletContext().getAttribute(DataHandler.USER.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        Long userId = Long.valueOf(request.getParameter("userId"));
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        User user = userDataHandler.findByUserId(userId);
        if (isUserNull(request, response, user)) {
            return;
        }
        if (isInvalidPassword(request, response, user.getPassword(), password)) {
            return;
        }
        User updateUser = new User(user.getUserId(), email, nickname, password, user.getCreatedDt());
        log.debug("[UserUpdateApi] user" + updateUser.toString());
        userDataHandler.update(updateUser);
        response.sendRedirect("/users/" + user.getUserId());
    }

    private boolean isUserNull(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        if (user == null) {
            request.setAttribute("status_code", HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "User 가 없습니다.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    private boolean isInvalidPassword(HttpServletRequest request, HttpServletResponse response, String userPassword,
                                      String inputPassword) throws ServletException, IOException {
        if (!userPassword.equals(inputPassword)) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "비밀번호가 맞지 않습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }
}
