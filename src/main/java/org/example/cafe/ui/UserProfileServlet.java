package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.example.cafe.application.UserService;
import org.example.cafe.application.dto.UserUpdateDto;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.domain.User;
import org.slf4j.Logger;

@WebServlet(name = "UserProfileServlet", value = "/users/*")
public class UserProfileServlet extends BaseServlet {

    private static final Logger log = getLogger(UserProfileServlet.class);

    private UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("UserService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 특정 회원 프로필 페이지를 반환한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String requestURI = request.getRequestURI();
        String[] pathParts = requestURI.split("/");
        String userId = URLDecoder.decode(pathParts[2], StandardCharsets.UTF_8);

        if (requestURI.endsWith("/form")) {
            forwardModifyForm(request, response, userId);
            return;
        }

        User user = userService.findById(userId);

        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
    }

    private void forwardModifyForm(HttpServletRequest request, HttpServletResponse response, String userId)
            throws IOException, ServletException {
        String loginUserId = (String) request.getSession().getAttribute("userId");
        if (!userId.equals(loginUserId)) {
            throw new BadAuthenticationException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        request.setAttribute("user", userService.findById(userId));
        request.getRequestDispatcher("/WEB-INF/user/update.jsp").forward(request, response);
    }

    /**
     * 회원 정보를 수정한다.
     *
     * @param request  an {@link HttpServletRequest} object that contains the request the client has made of the
     *                 servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        String userId = URLDecoder.decode(pathParts[2], StandardCharsets.UTF_8);

        if (!path.endsWith("/form")) {
            super.doPost(request, response);
            return;
        }

        String loginUserId = (String) request.getSession().getAttribute("userId");
        if (!userId.equals(loginUserId)) {
            throw new BadAuthenticationException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        String checkPassword = request.getParameter("checkPassword");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");

        User user = userService.updateUser(userId, new UserUpdateDto(checkPassword, password, nickname, email));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
    }
}