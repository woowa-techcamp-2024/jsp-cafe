package codesquad.global.servlet;

import codesquad.common.authorization.annotation.Authorized;
import codesquad.common.exception.DuplicateIdException;
import codesquad.common.exception.IncorrectPasswordException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.global.dao.UserQuery;
import codesquad.global.dao.UserQuery.QueryRequest;
import codesquad.global.dao.UserQuery.UserResponse;
import codesquad.user.domain.User;
import codesquad.user.service.SignUpService;
import codesquad.user.service.SignUpService.Command;
import codesquad.user.service.UpdateUserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UsersServlet.class);

    private UserQuery userQuery;
    private SignUpService signUpService;
    private UpdateUserService updateUserService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        userQuery = (UserQuery) servletContext.getAttribute("userQuery");
        signUpService = (SignUpService) servletContext.getAttribute("SignUpService");
        updateUserService = (UpdateUserService) servletContext.getAttribute("UpdateUserService");
        logger.info("UsersServlet initialized");
    }

    /**
     * 유저 리스트 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Listing all users");
        QueryRequest queryRequest = new QueryRequest(1, 10);
        List<UserResponse> userResponses = userQuery.findAll(queryRequest);
        req.setAttribute("users", userResponses);
        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(req, resp);
    }

    /**
     * 유저 등록 요청
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Registering user");
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        Command command = new Command(userId, password, name, email);
        try {
            signUpService.signUp(command);
        } catch (DuplicateIdException e) {
            req.setAttribute("errorMsg", e.getMessage());
            req.setAttribute("userId", userId);
            req.setAttribute("password", password);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    /**
     * 유저 수정 요청
     */
    @Authorized
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Updating user info");
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        try {
            UpdateUserService.Command command = new UpdateUserService.Command(Long.parseLong(id), password, name, email);
            updateUserService.update(command);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        } catch (NoSuchElementException e) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        } catch (IncorrectPasswordException e) {
            req.setAttribute("errorMsg", "잘못된 비밀번호 입니다.");
            req.setAttribute("user", new User(id, password, name, email));
            resp.sendRedirect(req.getContextPath() + "/users/" + id + "/update-form");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
