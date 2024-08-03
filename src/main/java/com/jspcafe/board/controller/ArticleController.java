package com.jspcafe.board.controller;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.service.ArticleService;
import com.jspcafe.user.model.User;
import com.jspcafe.util.HttpUtils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/", "/articles/*"})
public class ArticleController extends HttpServlet {

  private ArticleService articleService;

  @Override
  public void init(ServletConfig config) {
    ServletContext ctx = config.getServletContext();
    articleService = (ArticleService) ctx.getAttribute("articleService");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getPathInfo();
    if (path == null || path.isEmpty() || path.isBlank()) {
      articleList(req, resp);
      return;
    }
    if (path.endsWith("/form")) {
      articleForm(req, resp);
      return;
    }
    switch (path) {
      default -> articleDetail(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    writeArticle(req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    modifyArticle(req, resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    deleteArticle(req, resp);
  }

  private void forward(String fileName, HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/WEB-INF/views/board/" + fileName + ".jsp").forward(req, resp);
  }

  private void articleList(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    List<Article> articles = articleService.findAll();
    req.setAttribute("articles", articles);
    forward("board", req, resp);
  }

  private void articleForm(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getPathInfo()
        .replace("/form", "")
        .replace("/", "");
    if (id.isEmpty() || id.isBlank()) {
      forward("article_form", req, resp);
      return;
    }
    Article article = articleService.findById(id);
    User user = getUser(req);
    if (!article.userId().equals(user.id())) {
      resp.sendRedirect("/error/403");
      return;
    }
    req.setAttribute("article", article);
    forward("article_modify_form", req, resp);
  }

  private void writeArticle(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String title = req.getParameter("title");
    String content = req.getParameter("content");
    User user = getUser(req);
    articleService.write(user.id(), title, user.nickname(), content);
    resp.sendRedirect("/");
  }

  private void articleDetail(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getPathInfo().replace("/", "");
    Article article = articleService.findById(id);
    req.setAttribute("article", article);
    forward("article_detail", req, resp);
  }

  private User getUser(HttpServletRequest req) {
    HttpSession session = req.getSession();
    return (User) session.getAttribute("userInfo");
  }

  private void modifyArticle(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getPathInfo().replace("/", "");
    Map<String, Object> data = HttpUtils.getJsonRequestBody(req);
    articleService.update(id, (String) data.get("title"), (String) data.get("content"));
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private void deleteArticle(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getPathInfo().replace("/", "");
    Article article = articleService.findById(id);
    User user = getUser(req);
    if (!article.userId().equals(user.id())) {
      resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    boolean success = articleService.delete(id, user.id());
    if (success) {
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }
    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
  }
}
