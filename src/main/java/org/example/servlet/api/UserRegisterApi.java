package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.example.constance.DataHandler;
import org.example.data.UserDataHandler;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/users")
public class UserRegisterApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserRegisterApi.class);
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
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        if (isInvalidInput(request, response, email, nickname, password)) {
            return;
        }
        User findUser = userDataHandler.findByEmail(email);
        if (isDuplicateEmail(request, response, findUser)) {
            return;
        }
        User user = new User(email, nickname, password, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        User savedUser = userDataHandler.insert(user);
        log.debug("[UserRegisterApi]" + savedUser.toString());
        response.sendRedirect("/login");
    }


    private boolean isInvalidInput(HttpServletRequest request, HttpServletResponse response, String email,
                                   String nickname, String password) throws ServletException, IOException {
        if (!(email.length() < 255 && nickname.length() < 100 && password.length() < 255)) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message",
                    String.format("이메일 %d 글자, 닉네임 %d 글자, 비밀번호 %d 글자 보다 작은 지 확인 바랍니다", 255, 100, 255));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }


    private boolean isDuplicateEmail(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        if (user != null) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "해당 이메일에 사용자가 이미 있습니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }

}
