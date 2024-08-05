package codesqaud.app.service;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.db.TransactionManager;
import codesqaud.app.dto.PageDto;
import codesqaud.app.dto.ReplyDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

public class ReplyService {
    private static final int DEFAULT_PAGE_SIZE = 5;

    private final ArticleDao articleDao;
    private final ReplyDao replyDao;
    private final DataSource dataSource;

    public ReplyService(ArticleDao articleDao, ReplyDao replyDao, DataSource dataSource) {
        this.articleDao = articleDao;
        this.replyDao = replyDao;
        this.dataSource = dataSource;
    }

    /*
        GET Handlers
     */
    public void handleGetReplies(HttpServletRequest req, HttpServletResponse resp, Long articleId) throws IOException {
        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);
        ObjectMapper mapper = new ObjectMapper();

        Long pointer = getPointer(req);
        int size = getSize(req);
        int fetchSize = size + 1;

        List<ReplyDto> replies = replyDao.findPageWithPointer(articleId, pointer, fetchSize);
        boolean hasNext = false;
        if(replies.size() == fetchSize) {
            replies.remove(size);
            hasNext = true;
        }
        replies.forEach(replyDto -> replyDto.setMine(loginUser.getId()));
        long totalRepliesCount = replyDao.count(articleId);
        PageDto<ReplyDto> page = new PageDto<>(replies, hasNext, totalRepliesCount);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(SC_OK);
        resp.getWriter().write(mapper.writeValueAsString(page));
    }

    private int getSize(HttpServletRequest req) {
        String sizeParameter = req.getParameter("size");
        int size = DEFAULT_PAGE_SIZE;
        if (sizeParameter != null) {
            size = Integer.parseInt(sizeParameter);
        }
        return size;
    }

    private Long getPointer(HttpServletRequest req) {
        String pointerParameter = req.getParameter("pointer");
        Long pointer = Long.MAX_VALUE;
        if (pointerParameter != null) {
            pointer = Long.parseLong(pointerParameter);
        }
        return pointer;
    }

    /*
       POST Handlers
     */
    public void handleReplyCreation(HttpServletRequest req, HttpServletResponse resp, Long articleId) throws IOException {
        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);

        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = req.getInputStream();
        JsonNode jsonNode = objectMapper.readTree(inputStream);

        String contents = jsonNode.get("contents").asText();

        Reply reply = new Reply(contents, articleId, loginUser.getId());
        TransactionManager.executeTransaction(dataSource, () -> {
            Article article = articleDao.findByIdForUpdate(articleId)
                    .orElseThrow(() -> new HttpException(SC_NOT_FOUND));

            if (!article.isActivate()) {
                throw new HttpException(SC_FORBIDDEN, "댓글 작성 중 문제가 발생했습니다.");
            }
            replyDao.save(reply);
        });

        ReplyDto replyDto = ReplyDto.from(reply, loginUser);
        replyDto.setMine(loginUser.getId());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);

        String jsonResponse = objectMapper.writeValueAsString(replyDto);
        resp.getWriter().write(jsonResponse);
    }

    /*
        DELETE Handlers
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp, Long articleId, Long replyId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Reply reply = replyDao.findById(replyId).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!AuthenticationManager.isMe(req, reply.getAuthorId())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            String errorMessage = "본인의 댓글만 삭제할 수 있습니다.";
            String jsonResponse = objectMapper.writeValueAsString(errorMessage);
            resp.getWriter().write(jsonResponse);
            return;
        }

        replyDao.delete(reply);
        resp.setStatus(SC_OK);
        String message = "sucess";
        resp.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
