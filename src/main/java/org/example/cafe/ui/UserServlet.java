package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.cafe.application.UserService;
import org.example.cafe.application.dto.UserCreateDto;
import org.example.cafe.domain.User;
import org.slf4j.Logger;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends BaseServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("UserService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 회원 목록을 반환한다.
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        List<User> users = userService.findAll();

        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(request, response);
    }

    /**
     * 회원 가입한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");

        UserCreateDto userCreateDto = new UserCreateDto(userId, password, nickname, email);
        userService.createUser(userCreateDto);

        response.sendRedirect("/users");
    }
}