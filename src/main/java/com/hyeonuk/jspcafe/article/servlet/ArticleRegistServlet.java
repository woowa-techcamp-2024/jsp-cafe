package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.InvalidArticleRegistRequest;
import com.hyeonuk.jspcafe.member.domain.Member;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ArticleRegistServlet extends HttpServlet {
    private ArticleDao articleDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDao = (ArticleDao)config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        req.getRequestDispatcher("/templates/qna/form.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        if(session==null || session.getAttribute("member")==null){
            resp.sendRedirect("/login");
            return;
        }
        Member member = (Member)req.getSession().getAttribute("member");
        String writer = member.getMemberId();
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        Article article = new Article(writer, title, contents);

        if(!article.validation()) throw new InvalidArticleRegistRequest("빈값이 존재하면 안됩니다.");

        articleDao.save(article);
        resp.sendRedirect("/");
    }
}
