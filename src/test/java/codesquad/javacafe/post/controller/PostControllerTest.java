package codesquad.javacafe.post.controller;

import codesquad.javacafe.common.db.DBConnection;
import codesquad.javacafe.common.exception.ClientErrorCode;
import codesquad.javacafe.common.exception.CustomException;
import codesquad.javacafe.common.session.MemberInfo;
import codesquad.javacafe.member.entity.Member;
import codesquad.javacafe.member.repository.MemberRepository;
import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.dto.response.PostResponseDto;
import codesquad.javacafe.post.entity.Post;
import codesquad.javacafe.post.repository.PostRepository;
import codesquad.javacafe.util.CustomHttpServletRequest;
import codesquad.javacafe.util.CustomHttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PostControllerTest {

    private static PostController postController;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        postController = new PostController();
        DBConnection.setConnectionInfo("jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        connection = DBConnection.getConnection();
        createTable();
    }

    @AfterEach
    void afterEach() throws SQLException {
        clearTable();
    }

    static void createTable() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement statement = connection.createStatement();

        // Create the post table
        String createTableSql = "CREATE TABLE if not exists post (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "post_writer VARCHAR(255), " +
                "post_title VARCHAR(255), " +
                "post_contents TEXT, " +
                "post_create TIMESTAMP" +
                ")";
        statement.execute(createTableSql);

        String createMemberTableSql = "CREATE TABLE if not exists member (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "member_id VARCHAR(255), " +
                "member_password VARCHAR(255), " +
                "member_name VARCHAR(255)" +
                ")";
        statement.execute(createMemberTableSql);

        statement.close();
        connection.close();
    }

    private void clearTable() throws SQLException {
        var sql = "DELETE FROM post";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        sql = "DELETE FROM member";
        try(var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testDoProcessGet() throws ServletException, IOException {
        // Insert a post into the database
        Map<String, String[]> body = new HashMap<>();
        body.put("writer", new String[]{"writer1"});
        body.put("title", new String[]{"title1"});
        body.put("contents", new String[]{"contents1"});
        PostCreateRequestDto postDto = new PostCreateRequestDto(body);
        Post savePost = PostRepository.getInstance().save(postDto);

        // Simulate GET request with query parameters
        HttpServletRequest request = new CustomHttpServletRequest();
        HttpServletResponse response = new CustomHttpServletResponse();

        ((CustomHttpServletRequest) request).setMethod("GET");
        ((CustomHttpServletRequest) request).addParameter("postId",savePost.getId()+"");

        Member member = new Member("user1", "password", "User One");
        MemberRepository.getInstance().save(member);
        request.setAttribute("userId","user1");
        request.getSession().setAttribute("loginInfo", new MemberInfo(1, "user1", "User One"));

        postController.doProcess(request, response);

        PostResponseDto post = (PostResponseDto) request.getAttribute("post");
        assertNotNull(post);
        assertEquals("writer1", post.getWriter());
        assertEquals("/qna/show.jsp", ((CustomHttpServletResponse) response).getForwardedUrl());
    }

    @Test
    public void testDoProcessGetWithNonLogin() throws ServletException, IOException {
        // Insert a post into the database
        Map<String, String[]> body = new HashMap<>();
        body.put("writer", new String[]{"writer1"});
        body.put("title", new String[]{"title1"});
        body.put("contents", new String[]{"contents1"});
        PostCreateRequestDto postDto = new PostCreateRequestDto(body);
        PostRepository.getInstance().save(postDto);

        // Simulate GET request with query parameters
        HttpServletRequest request = new CustomHttpServletRequest();
        HttpServletResponse response = new CustomHttpServletResponse();

        ((CustomHttpServletRequest) request).setMethod("GET");
        ((CustomHttpServletRequest) request).addParameter("postId","1");


        CustomException exception = assertThrows(CustomException.class, () -> {
            postController.doProcess(request, response);
        });

        assertEquals(ClientErrorCode.UNAUTHORIZED_USER.getHttpStatus(), exception.getHttpStatus());
    }

    @Test
    public void testDoProcessPost() throws ServletException, IOException {
        // Simulate POST request
        HttpServletRequest request = new CustomHttpServletRequest();
        HttpServletResponse response = new CustomHttpServletResponse();

        ((CustomHttpServletRequest) request).setMethod("POST");
        ((CustomHttpServletRequest) request).addParameter("writer", "testWriter");
        ((CustomHttpServletRequest) request).addParameter("title", "testTitle");
        ((CustomHttpServletRequest) request).addParameter("contents", "testContents");

        postController.doProcess(request, response);

        assertEquals("/", ((CustomHttpServletResponse) response).getRedirectedUrl());

        List<Post> all = PostRepository.getInstance().findAll();

        // Verify the post was created
        Post post = PostRepository.getInstance().findById(all.get(0).getId());
        assertNotNull(post);
        assertEquals("testWriter", post.getWriter());
        assertEquals("testTitle", post.getTitle());
        assertEquals("testContents", post.getContents());
    }
}