package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.InMemoryPostRepository;
import com.codesquad.cafe.db.InMemoryUserRepository;
import com.codesquad.cafe.db.Page;
import com.codesquad.cafe.model.Post;
import com.codesquad.cafe.model.PostDetailsDto;
import com.codesquad.cafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(IndexServlet.class);

    private InMemoryPostRepository postRepository;

    private InMemoryUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (InMemoryPostRepository) getServletContext().getAttribute("postRepository");
        this.userRepository = (InMemoryUserRepository) getServletContext().getAttribute("userRepository");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumParam = req.getParameter("pageNum");
        String pageSizeParam = req.getParameter("pageSize");
        int pageNum = (pageNumParam == null || pageNumParam.isEmpty()) ? 1 : Integer.parseInt(pageNumParam);
        int pageSize = (pageSizeParam == null || pageSizeParam.isEmpty()) ? 5 : Integer.parseInt(pageSizeParam);

        Page<Post> posts = postRepository.findByPage(pageNum, pageSize);

        List<PostDetailsDto> postsWithDetail = posts.getContent().stream()
                .map(post -> {
                    User user = userRepository.findById(post.getUserId()).orElseThrow(()
                            -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
                    return new PostDetailsDto(post.getId(), post.getTitle(), post.getContent(),
                            post.getFileName(),
                            user.getId(), user.getUsername(), post.getCreatedAt());
                })
                .toList();

        Page<PostDetailsDto> page = Page.of(
                postsWithDetail,
                posts.getPageNumber(),
                posts.getPageSize(),
                posts.getActualSize(),
                posts.getTotalElements(),
                posts.getTotalPages()
        );

        req.setAttribute("page", page);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
