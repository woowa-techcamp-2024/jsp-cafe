package codesquad.user.handler;

import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import codesquad.user.handler.dao.UserQuery;
import codesquad.user.handler.dto.response.UserResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@Response
@RequestMapping("^/users/\\d+$")
public class UserHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    private final UserQuery userQuery;

    public UserHandler(UserQuery userQuery) {
        this.userQuery = userQuery;
    }

    /**
     * 유저 수정 폼 요청
     * 유저 정보 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting user info");
        long id;
        try {
            id = extractIdFromPath(req.getPathInfo());
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<UserResponse> findUserResponse = userQuery.findById(id);
        if (findUserResponse.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 유저입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("user", findUserResponse.get());
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    private long extractIdFromPath(String pathInfo) throws NumberFormatException {
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/update-form")) {
            return Long.parseLong(pathInfo.substring("/users/".length(), pathInfo.indexOf("/update-form")));
        }
        return Long.parseLong(pathInfo.substring("/users/".length()));
    }
}
