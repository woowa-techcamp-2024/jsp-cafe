package woopaca.jspcafe.config;

import woopaca.jspcafe.database.JdbcTemplate;
import woopaca.jspcafe.repository.PostMySQLRepository;
import woopaca.jspcafe.repository.PostRepository;
import woopaca.jspcafe.repository.ReplyMySQLRepository;
import woopaca.jspcafe.repository.ReplyRepository;
import woopaca.jspcafe.repository.UserMySQLRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.service.AuthService;
import woopaca.jspcafe.service.PostService;
import woopaca.jspcafe.service.ReplyService;
import woopaca.jspcafe.service.UserService;

public final class InstanceFactory {

    private static UserRepository userRepository;
    private static PostRepository postRepository;
    private static ReplyRepository replyRepository;
    private static UserService userService;
    private static PostService postService;
    private static AuthService authService;
    private static ReplyService replyService;
    private static JdbcTemplate jdbcTemplate;

    private InstanceFactory() {
    }

    public static UserRepository userRepository() {
        if (userRepository == null) {
            userRepository = new UserMySQLRepository(jdbcTemplate());
        }
        return userRepository;
    }

    public static PostRepository postRepository() {
        if (postRepository == null) {
            postRepository = new PostMySQLRepository(jdbcTemplate());
        }
        return postRepository;
    }

    public static ReplyRepository replyRepository() {
        if (replyRepository == null) {
            replyRepository = new ReplyMySQLRepository(jdbcTemplate());
        }
        return replyRepository;
    }

    public static UserService userService() {
        if (userService == null) {
            userService = new UserService(userRepository());
        }
        return userService;
    }

    public static PostService postService() {
        if (postService == null) {
            postService = new PostService(postRepository(), userRepository());
        }
        return postService;
    }

    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate();
        }
        return jdbcTemplate;
    }

    public static AuthService authService() {
        if (authService == null) {
            authService = new AuthService(userRepository());
        }
        return authService;
    }

    public static ReplyService replyService() {
        if (replyService == null) {
            replyService = new ReplyService(replyRepository(), postRepository());
        }
        return replyService;
    }
}
