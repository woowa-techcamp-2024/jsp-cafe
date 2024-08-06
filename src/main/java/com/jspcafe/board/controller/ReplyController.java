package com.jspcafe.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jspcafe.board.model.Reply;
import com.jspcafe.board.service.ReplyService;
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

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {

  private ReplyService replyService;
  private ObjectMapper objectMapper;

  @Override
  public void init(ServletConfig config) throws ServletException {
    ServletContext ctx = config.getServletContext();
    replyService = (ReplyService) ctx.getAttribute("replyService");
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String articleId = req.getParameter("articleId");
    int page = Integer.parseInt(req.getParameter("page"));
    List<Reply> replies = replyService.findByArticleId(articleId, page);
    sendJsonResponse(resp, replies);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, Object> data = HttpUtils.getJsonRequestBody(req);
    User user = getUser(req);
    if (user == null) {
      resp.sendRedirect("/users/login");
      return;
    }
    Reply newReply = replyService.save((String) data.get("articleId"), user.id(), user.nickname(),
        (String) data.get("content"));
    sendJsonResponse(resp, newReply);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Map<String, Object> data = HttpUtils.getJsonRequestBody(req);
    String id = (String) data.get("id");
    Reply reply = replyService.findById(id);
    User user = getUser(req);
    if (!reply.userId().equals(user.id())) {
      resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    Reply updatedReply = replyService.update(id, (String) data.get("content"));
    sendJsonResponse(resp, updatedReply);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getPathInfo().substring(1);
    User user = getUser(req);
    Reply reply = replyService.findById(id);
    if (!reply.userId().equals(user.id())) {
      resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    boolean success = replyService.delete(id);
    sendJsonResponse(resp, success);
  }

  private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(resp.getWriter(), data);
  }

  private User getUser(HttpServletRequest req) {
    HttpSession session = req.getSession();
    return (User) session.getAttribute("userInfo");
  }
}
