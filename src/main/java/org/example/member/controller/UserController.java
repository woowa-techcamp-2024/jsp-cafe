package org.example.member.controller;

import java.sql.SQLException;
import java.util.List;
import org.example.config.HttpMethod;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Controller;
import org.example.config.annotation.PathVariable;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.config.mv.ModelAndView;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.service.UserQueryService;
import org.example.member.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserQueryService userQueryService;

    @Autowired
    public UserController(UserService userService, UserQueryService userQueryService) {
        this.userService = userService;
        this.userQueryService = userQueryService;
    }

    @RequestMapping(method = HttpMethod.GET)
    public ModelAndView getUserList() throws SQLException {
        ModelAndView mv = new ModelAndView("user/UserList");
        List<UserResponseDto> allUsers = userQueryService.findAllUsers();
        mv.addAttribute("users", allUsers);
        return mv;
    }

    @RequestMapping(path = "/{id}/form", method = HttpMethod.GET)
    public ModelAndView showEditForm(@PathVariable(value = "id") String userId) throws SQLException {
        logger.info("userId: {}", userId);
        ModelAndView mv = new ModelAndView("user/UserEditForm");
        UserResponseDto response = userQueryService.findUserByUserId(userId);
        mv.addAttribute("user", response);
        return mv;
    }

    @RequestMapping(path = "/{id}", method = HttpMethod.GET)
    public ModelAndView showUserProfile(@PathVariable(value = "id") String userId) throws SQLException {
        logger.info("userId: {}", userId);
        ModelAndView mv = new ModelAndView("user/UserProfile");
        UserResponseDto response = userQueryService.findUserByUserId(userId);
        mv.addAttribute("user", response);
        return mv;
    }

    @RequestMapping(method = HttpMethod.POST)
    public ModelAndView registerUser(@RequestParam("userId") String userId,
                                     @RequestParam("password") String password,
                                     @RequestParam("name") String name,
                                     @RequestParam("email") String email) throws SQLException {
        ModelAndView mv = new ModelAndView("redirect:/users");

        User user = User.createUser(userId, password, name, email);
        userService.register(user);
        return mv;
    }

    @RequestMapping(path = "/{id}", method = HttpMethod.POST)
    public ModelAndView editUserProfile(@PathVariable("id") String profileUser,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("password") String password,
                                        @RequestParam("name") String name,
                                        @RequestParam("email") String email) throws SQLException {
        ModelAndView mv = new ModelAndView("redirect:/users");
        User user = User.createUser(userId, password, name, email);
        if (!profileUser.equals(userId)) {
            throw new RuntimeException();
        }
        userService.editUser(profileUser, user);

        return mv;
    }
}
