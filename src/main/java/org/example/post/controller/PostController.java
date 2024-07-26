package org.example.post.controller;

import java.sql.SQLException;
import java.util.List;
import org.example.config.HttpMethod;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Controller;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.config.mv.ModelAndView;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(path = "/", method = HttpMethod.GET)
    public ModelAndView list() throws SQLException {
        List<PostResponse> postResponses = postService.getAll();
        ModelAndView mv = new ModelAndView("post/PostList");
        mv.addAttribute("posts", postResponses);

        return mv;
    }

    @RequestMapping(path = "/questions", method = HttpMethod.GET)
    public ModelAndView getQuestions(@RequestParam("id") Long id) throws SQLException {
        logger.info("questId: {}", id);
        PostResponse post = postService.getPostById(id);
        ModelAndView mv = new ModelAndView("post/PostDetail");
        mv.addAttribute("post", post);
        return mv;
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
}

