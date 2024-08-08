package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.util.SessionUtil.getUserPrincipal;

import com.codesquad.cafe.db.dao.CommentDao;
import com.codesquad.cafe.db.domain.Comment;
import com.codesquad.cafe.exception.AuthorizationException;
import com.codesquad.cafe.exception.ResourceNotFoundException;
import com.codesquad.cafe.model.UserPrincipal;
import com.codesquad.cafe.model.aggregate.CommentWithUser;
import com.codesquad.cafe.model.dto.CommentCreateRequest;
import com.codesquad.cafe.model.dto.CommentsResponse;
import com.codesquad.cafe.util.JsonModelMapper;
import com.codesquad.cafe.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private CommentDao commentDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.commentDao = (CommentDao) getServletContext().getAttribute("commentDao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = getUserPrincipal(req);

        CommentCreateRequest requestDto = JsonModelMapper.map(req, CommentCreateRequest.class);
        requestDto.validate();

        Comment comment = commentDao.save(requestDto.toComment(userPrincipal.getId()));
        Optional<CommentWithUser> commentWithUser = commentDao.findCommentWithUserById(comment.getId());

        JsonModelMapper.json(resp, commentWithUser.get());
    }

    //
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);
//
//        Long postId = parsePathVariable(req.getPathInfo());
//        PostUpdateRequest requestDto = JsonModelMapper.map(req, PostUpdateRequest.class);
//        requestDto.validate();
//
//        // find original post
//        Post post = postRepository.findById(postId)
//                .orElseThrow(ResourceNotFoundException::new);
//
//        // validate request
//        if (!post.getAuthorId().equals(requestDto.getAuthorId())) {
//            resp.setStatus(400);
//            JsonModelMapper.json(resp, new ErrorResponse("작성자가 일치하지 않습니다."));
//        }
//
//        // authorization
//        if (userPrincipal == null || !userPrincipal.getId().equals(post.getAuthorId())) {
//            throw new AuthorizationException();
//        }
//
//        // update post
//        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getFileName());
//        postRepository.save(post);
//        PostWithAuthor postWithAuthor = postRepository.findPostWithAuthorById(postId)
//                .orElseThrow(ResourceNotFoundException::new);
//
//        req.setAttribute("post", postWithAuthor);
//        JsonModelMapper.json(resp, new RedirectResponse("/"));
//    }
//
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserPrincipal userPrincipal = SessionUtil.getUserPrincipal(req);

        Long id = parsePathVariable(req.getPathInfo());

        Comment comment = commentDao.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (userPrincipal == null || !userPrincipal.getId().equals(comment.getUserId())) {
            throw new AuthorizationException();
        }

        comment.delete();
        commentDao.save(comment);
    }

    private Long parsePathVariable(String pathInfo) {
        try {
            String[] paths = pathInfo.substring(1).split("/");
            return Long.parseLong(paths[0]);
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException();
        }
    }

}
