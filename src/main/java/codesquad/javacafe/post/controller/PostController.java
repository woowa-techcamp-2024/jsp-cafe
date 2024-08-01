package codesquad.javacafe.post.controller;

import codesquad.javacafe.common.SubController;
import codesquad.javacafe.common.exception.ClientErrorCode;
import codesquad.javacafe.common.exception.HttpStatus;
import codesquad.javacafe.common.session.MemberInfo;
import codesquad.javacafe.common.session.SessionManager;
import codesquad.javacafe.post.cache.PostCache;
import codesquad.javacafe.post.dto.request.PostRequestDto;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.service.PostService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class PostController implements SubController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[PostController doProcess]");
        var method = req.getMethod();
        log.info("[PostController doProcess] method: {}", method);
        switch (method) {
            case "GET":{
                var userId = (String)req.getAttribute("userId");
                log.debug("UserId = {}",userId);
                SessionManager.getInstance().loginCheck(req,"loginInfo");

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
                var dispatcher = req.getRequestDispatcher("/WEB-INF/qna/show.jsp");
                dispatcher.forward(req, res);
                break;
            }
            case "POST" :{
                var body = req.getParameterMap();
                var hiddenMethod = body.get("method");
                log.debug("HiddenMethod: {}", Arrays.toString(hiddenMethod));
                log.debug("non null? {}",Objects.nonNull(hiddenMethod));
                if (Objects.nonNull(hiddenMethod)&&(Objects.equals(hiddenMethod[0], "PUT") || Objects.equals(hiddenMethod[0], "DELETE"))) {
                    log.debug("hiddenMethod: {}", hiddenMethod[0]);
                    method = hiddenMethod[0];
                } else {
                    createPost(req);
                    res.sendRedirect("/");
                    break;
                }
            }
            case "PUT":{
                var postRequestDto = getPostRequestDto(req);
                var writer = SessionManager.getInstance().getMemberName(req, "loginInfo");
                postRequestDto.setWriter(writer);
                var memberId = SessionManager.getInstance().getMemberId(req,"loginInfo");
                if (memberId != postRequestDto.getMemberId()) {
                    throw ClientErrorCode.POST_ACCESS_DENIED.customException("memberId = " + memberId + ", request info = " + postRequestDto);
                }

                PostService.updatePost(postRequestDto);
                // 캐시 업데이트
                PostCache.getInstance().updateCache(postRequestDto);

                req.setAttribute("post", PostCache.getInstance().get(postRequestDto.getId()));
                var dispatcher = req.getRequestDispatcher("/WEB-INF/qna/show.jsp");
                dispatcher.forward(req, res);

                break;
            }
            case "DELETE":{

            }
            default:
                throw ClientErrorCode.PAGE_NOT_FOUND.customException("request uri = " + req.getRequestURI() + ", request method = " + method);
        }
    }

    private void createPost(HttpServletRequest req) {
        var body = req.getParameterMap();
        var postDto = new PostRequestDto(body);
        var writer = SessionManager.getInstance().getMemberName(req, "loginInfo");
        var memberId = SessionManager.getInstance().getMemberId(req,"loginInfo");
        postDto.setWriter(writer);
        postDto.setMemberId(memberId);
        log.info("[PostController createPost] postRequestDto: {}", postDto);
        PostService.getInstance().createPost(postDto);
    }

    private PostRequestDto getPostRequestDto(HttpServletRequest req) throws IOException {
        var body = req.getParameterMap();

        var id = Long.parseLong(body.get("postId")[0]);
        var title = body.get("title")[0];
        var contents = body.get("contents")[0];
        var memberId = Long.parseLong(body.get("memberId")[0]);

        log.debug("[PostController doProcess] id : {}, title: {}, contents: {}", id, title, contents);

        return new PostRequestDto(id,title, contents,memberId);
    }
}
