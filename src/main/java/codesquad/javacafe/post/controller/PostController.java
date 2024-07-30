package codesquad.javacafe.post.controller;

import codesquad.javacafe.common.SubController;
import codesquad.javacafe.post.cache.PostCache;
import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.repository.PostRepository;
import codesquad.javacafe.post.service.PostService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PostController implements SubController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[PostController doProcess]");
        var method = req.getMethod();
        log.info("[PostController doProcess] method: {}", method);
        switch (method) {
            case "GET":{
                var body = Long.parseLong(req.getParameterMap().get("postId")[0]);
                log.debug("[PostController doProcess] body: {}", body);

                var postCache = PostCache.getInstance().get(body);
                if (postCache != null) {
                    log.debug("[PostController] Cache Hit: {}", postCache);
                    req.setAttribute("post", postCache);
                } else {
                    var post = PostService.getInstance().getPost(body);
                    PostCache.getInstance().set(body, post);
                    log.debug("[PostController doProcess] post: {}", post);
                    req.setAttribute("post", post);
                }
                var dispatcher = req.getRequestDispatcher("/qna/show.jsp");
                dispatcher.forward(req, res);
                break;
            }
            case "POST" :{
                createPost(req);
                res.sendRedirect("/");
                break;
            }
        }
    }

    private void createPost(HttpServletRequest req) {
        var body = req.getParameterMap();
        var postDto = new PostCreateRequestDto(body);
        log.info("[PostController createPost] postRequestDto: {}", postDto);
        PostService.getInstance().createPost(postDto);
    }
}
