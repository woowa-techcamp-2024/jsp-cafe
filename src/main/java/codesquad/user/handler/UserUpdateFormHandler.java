package codesquad.user.handler;

import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.global.dao.UserQuery;
import codesquad.global.servlet.annotation.RequestMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("^/users/\\d+/update-form$")
public class UserUpdateFormHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserUpdateFormHandler.class);

    private final UserQuery userQuery;

    public UserUpdateFormHandler(UserQuery userQuery) {
        this.userQuery = userQuery;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("UserUpdateForm serve");
        long id;
        try {
            id = extractIdFromPath(req.getPathInfo());
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<UserQuery.UserResponse> findUserResponse = userQuery.findById(id);
        if (findUserResponse.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", findUserResponse.get());
        req.getRequestDispatcher("/WEB-INF/views/user/update-form.jsp").forward(req, resp);
    }

    private long extractIdFromPath(String pathInfo) throws NumberFormatException {
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/update-form")) {
            return Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/update-form")));
        }
        return Long.parseLong(pathInfo.substring(1));
    }
}
