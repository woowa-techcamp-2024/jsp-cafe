package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ArticleControlServlet extends HttpServlet {
    private ArticleDao articleDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || "/".equals(pathInfo)) {
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        String[] pathParts = pathInfo.split("/");
        if(pathParts.length < 2){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        try {
            Long articleId = Long.parseLong(pathParts[1]);

            Article article = articleDao.findById(articleId)
                    .orElseThrow(() -> new HttpNotFoundException(articleId + "번의 게시글을 찾을 수 없습니다."));
            req.setAttribute("article",article);

            if(pathParts.length == 2) {
                req.getRequestDispatcher("/templates/qna/show.jsp").forward(req, resp);
            }
            else if(pathParts.length == 3 && "form".equals(pathParts[2])){
                HttpSession session = req.getSession();
                Member member = (Member)session.getAttribute("member");
                if(!article.getWriter().equals(member.getMemberId())) {
                    throw new HttpBadRequestException("작성자가 아닙니다.");
                }
                req.getRequestDispatcher("/templates/qna/modify.jsp").forward(req, resp);
            }
            else {
                throw new HttpBadRequestException("잘못된 요청입니다.");
            }
        }catch(NumberFormatException e ){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || pathInfo.isEmpty() || pathInfo.split("/").length != 2) {
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
        try {
            Long articleId = Long.parseLong(pathInfo.substring(1));

            Article article = articleDao.findById(articleId)
                    .orElseThrow(() -> new HttpNotFoundException(articleId + "번의 게시글을 찾을 수 없습니다."));

            Member member = (Member)req.getSession().getAttribute("member");
            if(!article.getWriter().equals(member.getMemberId())) throw new HttpBadRequestException("작성자가 아니라 권한이 없습니다.");

            String method = req.getParameter("_method");
            if("PUT".equalsIgnoreCase(method)){
                //수정 메서드
                String title = req.getParameter("title");
                String contents = req.getParameter("contents");

                article.setContents(contents);
                article.setTitle(title);

                if(!article.validation()) throw new HttpBadRequestException("빈 값이 있으면 안됩니다.");

                articleDao.save(article);
                resp.sendRedirect("/questions/"+article.getId());
            }
            else if("DELETE".equalsIgnoreCase(method)){
                //삭제 메서드

            }

        }catch(NumberFormatException e ){
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
    }
}
