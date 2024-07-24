package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.dto.request.CreateUserRequest;
import woowa.cafe.service.UserService;
import woowa.frame.web.annotation.Router;
import woowa.frame.web.annotation.HttpMapping;

import java.util.List;

@Router
public class UserRouter {

    private final UserService userService;

    public UserRouter(UserService userService) {
        this.userService = userService;
    }

    @HttpMapping(method = "GET", urlTemplate = "/users")
    public String getUsers(HttpServletRequest request, HttpServletResponse response) {
        List<UserInfo> users = userService.getUsers();
        request.setAttribute("users", users);
        return "/template/user/list.jsp";
    }

    @HttpMapping(method = "POST", urlTemplate = "/user/create")
    public String createUser(HttpServletRequest request, HttpServletResponse response) {
        var createUserRequest = new CreateUserRequest(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        userService.createUser(createUserRequest);
        return "redirect:/static/user/list.html";
    }

    @HttpMapping(method = "GET", urlTemplate = "/user/{userId}")
    public String getProfile(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getRequestURI().substring(6);
        UserInfo user = userService.getProfile(userId);

        if(user == null){
            return "redirect:/error/404.html";
        } else {
            request.setAttribute("user", user);
            return "/template/user/profile.jsp";
        }
    }
}
