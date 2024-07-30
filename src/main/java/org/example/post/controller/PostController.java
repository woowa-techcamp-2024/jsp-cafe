package org.example.post.controller;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import org.example.config.HttpMethod;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Controller;
import org.example.config.annotation.PathVariable;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.config.mv.ModelAndView;
import org.example.member.model.dto.UserResponseDto;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.service.PostService;
import org.example.util.session.InMemorySessionManager;
import org.example.util.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final SessionManager sessionManager;

    @Autowired
    public PostController(PostService postService, SessionManager sessionManager) {
        this.postService = postService;
        this.sessionManager = sessionManager;
    }

    @RequestMapping(path = "/", method = HttpMethod.GET)
    public ModelAndView list() throws SQLException {
        List<PostResponse> postResponses = postService.getAll();
        ModelAndView mv = new ModelAndView("post/PostList");
        mv.addAttribute("posts", postResponses);

        return mv;
    }

    @RequestMapping(path = "/questions", method = HttpMethod.GET)
    public ModelAndView getQuestionForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("/post/PostForm");
        UserResponseDto userDetails = sessionManager.getUserDetails(session.getId());
        if (userDetails == null) {
            return new ModelAndView("redirect:/user/login");
        }
        mav.addAttribute("userName", userDetails.getName());
        return mav;
    }

    @RequestMapping(path = "/questions", method = HttpMethod.POST)
    public ModelAndView addQuestion(@RequestParam("writer") String writer,
                                    @RequestParam("title") String title,
                                    @RequestParam("contents") String contents) throws SQLException {
        Post post = Post.create(writer, title, contents);
        postService.create(post);
        ModelAndView mv = new ModelAndView("redirect:/");
        return mv;
    }

    @RequestMapping(path = "/questions/{id}", method = HttpMethod.GET)
    public ModelAndView getQuestion(@PathVariable("id") Long id) throws SQLException {
        ModelAndView mv = new ModelAndView("post/PostDetail");
        PostResponse post = postService.getPostById(id);
        mv.addAttribute("post", post);
        return mv;
    }
}

