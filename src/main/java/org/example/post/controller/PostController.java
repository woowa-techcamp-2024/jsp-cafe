package org.example.post.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
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
    public ModelAndView addQuestion(@RequestParam("title") String title,
                                    @RequestParam("contents") String contents,
                                    HttpSession session) throws SQLException {
        UserResponseDto userDetails = sessionManager.getUserDetails(session.getId());
        Post post = Post.create(userDetails.getName(), title, contents);
        postService.create(post);
        ModelAndView mv = new ModelAndView("redirect:/");
        return mv;
    }

    @RequestMapping(path = "/questions/{id}", method = HttpMethod.GET)
    public ModelAndView getQuestion(@PathVariable("id") Long id, HttpSession session) throws SQLException {
        ModelAndView mv = new ModelAndView("post/PostDetail");
        PostResponse post = postService.getPostById(id);
        mv.addAttribute("isAuthor", isAuthor(session, post));
        mv.addAttribute("post", post);
        return mv;
    }

    @RequestMapping(path = "/questions/{id}", method = HttpMethod.PUT)
    public void editQuestion(@PathVariable("id") Long id, @RequestParam("title") String title, @RequestParam("contents") String contents,
                                     HttpServletResponse response) throws SQLException, IOException {
        PostResponse post = postService.getPostById(id);
        post.setTitle(title);
        post.setContents(contents);
        postService.updatePost(post);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("X-Redirect-Location", "/questions/" + id);
        response.getWriter().write("게시글이 성공적으로 수정되었습니다.");
    }

    @RequestMapping(path = "/questions/{id}/form", method = HttpMethod.GET)
    public ModelAndView getQuestionEditForm(@PathVariable("id") Long id, HttpSession session) throws SQLException {
        ModelAndView mv = new ModelAndView("post/PostEditForm");
        PostResponse post = postService.getPostById(id);
        boolean isAuthor = isAuthor(session, post);
        if (!isAuthor) {
            return new ModelAndView("redirect:/questions/" + id);
        }
        mv.addAttribute("isAuthor", isAuthor);
        mv.addAttribute("post", post);
        return mv;
    }

    private boolean isAuthor(HttpSession session, PostResponse post) {
        boolean isAuthor = false;

        if (session != null) {
            UserResponseDto userDetails = sessionManager.getUserDetails(session.getId());
            isAuthor = userDetails.getName().equals(post.getWriter());
            logger.info("isAuthor: " + isAuthor);
        }
        return isAuthor;
    }
}

