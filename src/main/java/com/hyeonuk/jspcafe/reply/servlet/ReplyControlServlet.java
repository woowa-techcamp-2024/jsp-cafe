package com.hyeonuk.jspcafe.reply.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.domain.Page;
import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.reply.dao.ReplyDao;
import com.hyeonuk.jspcafe.reply.domain.Reply;
import com.hyeonuk.jspcafe.reply.dto.ReplyDto;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplyControlServlet extends HttpServlet {
    private ReplyDao replyDao;
    private ArticleDao articleDao;
    private ObjectMapper objectMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        replyDao = (ReplyDao) config.getServletContext().getAttribute("replyDao");
        articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String pathInfo = req.getPathInfo();
            if(pathInfo == null || "/".equals(pathInfo)) {
                throw new HttpBadRequestException("잘못된 요청입니다.");
            }
            String[] pathParts = pathInfo.split("/");
            if(pathParts.length < 2){
                throw new HttpBadRequestException("잘못된 요청입니다.");
            }
            String articleIdString = pathParts[1].indexOf('?') != -1 ? pathParts[1].substring(0,pathParts[1].indexOf('?')) : pathParts[1];
            Long articleId = Long.parseLong(articleIdString);
            Article article = articleDao.findById(articleId)
                    .orElseThrow(() -> new HttpNotFoundException("게시글을 찾을 수 없습니다."));

            String pageString = req.getParameter("page");
            String sizeString = req.getParameter("size");
            int page =  pageString == null || pageString.isBlank()  ? 1 : Integer.parseInt(pageString);
            int size =  sizeString == null || sizeString.isBlank() ? 5 : Integer.parseInt(sizeString);

            long count = replyDao.countByArticleId(articleId);
            List<ReplyDto> replies = replyDao.findAllByArticleId(article.getId(),size,page)
                    .stream()
                    .map(ReplyDto::from)
                    .collect(Collectors.toList());

            Page<ReplyDto> result = new Page(size,page,count,replies);

            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(result));
            resp.getWriter().flush();
        }catch(NumberFormatException e){
            e.printStackTrace();
            throw new HttpBadRequestException("잘못된 요청입니다.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Member member = (Member) req.getSession().getAttribute("member");
            //등록
            String reqBody = new String(req.getInputStream().readAllBytes());
            ReplyDto request = objectMapper.readValue(reqBody, ReplyDto.class);

            String contents = request.getContents();
            Long articleId = request.getArticleId();
            Reply reply = new Reply(new Article(articleId, null, null, null),
                    member, contents);

            replyDao.save(reply);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(ReplyDto.from(reply)));
            resp.getWriter().flush();

        }catch(HttpBadRequestException e){
            String message = e.getMessage();
            Map<String,String> ret = Map.of("message",message);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ret));
            resp.getWriter().flush();
        }catch (Exception e){
            Map<String,String> ret = Map.of("message","서버 내부 에러입니다.");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ret));
            resp.getWriter().flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Member member = (Member) req.getSession().getAttribute("member");
            //삭제
            String pathInfo = req.getPathInfo();
            if(pathInfo == null || "/".equals(pathInfo)) {
                throw new HttpBadRequestException("잘못된 요청입니다.");
            }
            String[] pathParts = pathInfo.split("/");
            if(pathParts.length < 2){
                throw new HttpBadRequestException("잘못된 요청입니다.");
            }
            Long replyId = Long.parseLong(pathParts[1]);

            Reply reply = replyDao.findById(replyId)
                    .orElseThrow(() -> new HttpBadRequestException("reply가 없습니다."));

            if(reply.getMember().getId() != member.getId()){
                throw new HttpBadRequestException("자신의 댓글이 아닙니다.");
            }

            replyDao.deleteById(reply.getId());
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(new HashMap<>()));
        }catch(HttpBadRequestException e){
            String message = e.getMessage();
            Map<String,String> ret = Map.of("message",message);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ret));
            resp.getWriter().flush();
        }catch (Exception e){
            Map<String,String> ret = Map.of("message","서버 내부 에러입니다.");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ret));
            resp.getWriter().flush();
        }
    }
}
