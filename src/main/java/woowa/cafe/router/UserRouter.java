package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.dto.request.CreateUserRequest;
import woowa.cafe.dto.request.UpdateUserRequest;
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
        return "redirect:/users";
    }

    @HttpMapping(method = "GET", urlTemplate = "/user/{userId}")
    public String getProfile(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getRequestURI().substring(6);
        UserInfo user = userService.getProfile(userId);

        if (user == null) {
            return "redirect:/error/404.html";
        } else {
            request.setAttribute("user", user);
            return "/template/user/profile.jsp";
        }
    }

    @HttpMapping(method = "GET", urlTemplate = "/user")
    public String getUserFormPage(HttpServletRequest request, HttpServletResponse response) {
        return "/template/user/form.jsp";
    }

    @HttpMapping(method = "GET", urlTemplate = "/user/{userId}/form")
    public String editProfilePage(HttpServletRequest request, HttpServletResponse response) {
        return "/template/user/editProfile.jsp";
    }

    @HttpMapping(method = "GET", urlTemplate = "/user/form")
    public String redirectUserPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        if(userInfo == null) return "redirect:/login";
        return "redirect:/user/" + userInfo.id() + "/form";
    }

    @HttpMapping(method = "POST", urlTemplate = "/user/{userId}/edit")
    public String editProfile(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getRequestURI().substring(6, request.getRequestURI().length() - 5);
        var req = new UpdateUserRequest(
                userId,
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );

        if(request.getSession(false) == null) {
            return "redirect:/login";
        }

        try {
            userService.updateUser(req);
            return "redirect:/user/" + userId;
        } catch (RuntimeException ex) {
            return "/template/user/editProfile.jsp";
        }
    }
}
