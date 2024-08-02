package codesquad.javacafe.comment.controller;

import codesquad.javacafe.comment.dto.request.CommentRequestDto;
import codesquad.javacafe.comment.dto.response.CommentResponseDto;
import codesquad.javacafe.comment.service.CommentService;
import codesquad.javacafe.comment.util.LocalDateTimeSerializer;
import codesquad.javacafe.common.SubController;
import codesquad.javacafe.common.exception.ClientErrorCode;
import codesquad.javacafe.common.exception.ServerErrorCode;
import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CommentController implements SubController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[CommentController doProcess]");
        var method = req.getMethod();
        log.info("[CommentController doProcess] Method: {}", method);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();

        switch (method){
            case "GET":{
                var postId = Long.parseLong(req.getParameter("postId"));
                var commentList = CommentService.getInstance().getCommentList(postId);

                var jsonResponse = gson.toJson(commentList);
                sendResponse(res, jsonResponse);

                break;
            }
            case "POST":{
                Map<String, Object> body = getBody(req);
                var commentRequestDto = new CommentRequestDto(body);
                log.debug("[CommentController Save] commentRequestDto: {}", commentRequestDto);
                var commentResponseDto = CommentService.getInstance().save(commentRequestDto);
                log.debug("[CommentController Response DTO] commentResponseDto: {}", commentResponseDto);

                var jsonResponse = gson.toJson(commentResponseDto);
                log.debug("[CommentController Response JSON] jsonResponse: {}", jsonResponse);

                sendResponse(res, jsonResponse);
                break;
            }
            default: throw ClientErrorCode.METHOD_NOT_ALLOWED.customException("request uri = "+ req.getRequestURI() + ", request method = "+method);
        }
    }

    private void sendResponse(HttpServletResponse res, String jsonResponse) {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setStatus(HttpServletResponse.SC_CREATED);

        try {
            res.getWriter().write(jsonResponse);
        } catch (IOException exception) {
            throw ServerErrorCode.INTERNAL_SERVER_ERROR.customException(exception.getMessage());
        }
    }

    private Map<String,Object> getBody(HttpServletRequest req) {
        try {
            Map<String, Object> data = new HashMap<>();

            log.debug("input json");
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = new  BufferedReader(new InputStreamReader(req.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonBuffer.toString(), JsonObject.class);

            log.debug("data get");
            // Extract data from JSON object
            long postId = jsonObject.get("postId").getAsLong();
            long memberId = jsonObject.get("memberId").getAsLong();
            String comment = jsonObject.get("comment").getAsString();
            log.debug("data get success");
            data.put("postId", postId);
            data.put("memberId", memberId);
            data.put("comment", comment);

            log.debug("data = {}",data);


            return data;
        } catch (IOException exception) {
            throw ServerErrorCode.INTERNAL_SERVER_ERROR.customException("exception occurred while reading request body, exception message = "+exception.getMessage());
        }
    }
}
