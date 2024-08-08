package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.dao.Page;
import com.codesquad.cafe.db.dao.PostRepository;
import com.codesquad.cafe.db.dao.UserRepository;
import com.codesquad.cafe.model.aggregate.PostWithAuthor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private PostRepository postRepository;

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postRepository = (PostRepository) getServletContext().getAttribute("postRepository");
        this.userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pageNum = getRequestedPageNum(req);
        int pageSize = getRequestedPageSize(req);

        try {
            Page<PostWithAuthor> page = postRepository.findPostWithAuthorByPageSortByCreatedAtDesc(pageNum, pageSize);
            req.setAttribute("page", page);
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            resp.sendError(400);
        }
    }

    private int getRequestedPageNum(HttpServletRequest req) {
        String pageNumParam = req.getParameter("pageNum");
        return (pageNumParam == null || pageNumParam.isEmpty()) ? 1 : Integer.parseInt(pageNumParam);
    }

    private int getRequestedPageSize(HttpServletRequest req) {
        String pageSizeParam = req.getParameter("pageSize");
        return (pageSizeParam == null || pageSizeParam.isEmpty()) ? 5 : Integer.parseInt(pageSizeParam);
    }

}
