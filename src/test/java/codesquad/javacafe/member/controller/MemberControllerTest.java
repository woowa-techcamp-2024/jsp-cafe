package codesquad.javacafe.member.controller;

import codesquad.javacafe.common.db.DBConnection;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.response.MemberResponseDto;
import codesquad.javacafe.member.repository.MemberRepository;
import codesquad.javacafe.member.service.MemberService;
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

public class MemberControllerTest {

    private static MemberController memberController;
    private static Connection connection;
    private static MemberRepository memberRepository;

    @BeforeAll
    static void setUp() throws SQLException {
        memberController = new MemberController();
        DBConnection.setConnectionInfo("jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        connection = DBConnection.getConnection();
        createTable();
        memberRepository = MemberRepository.getInstance();
    }

    @AfterEach
    void afterEach() throws SQLException {
        clearTable();
    }

    static void createTable() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement statement = connection.createStatement();

        // Create the member table
        String createTableSql = "CREATE TABLE if not exists member (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "member_id VARCHAR(255), " +
                "member_password VARCHAR(255), " +
                "member_name VARCHAR(255)" +
                ")";
        statement.execute(createTableSql);

        statement.close();
        connection.close();
    }

    private void clearTable() throws SQLException {
        var sql = "DELETE FROM member";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testDoProcessGet() throws ServletException, IOException {
        Map<String, String[]> body = new HashMap<>();
        body.put("userId", new String[]{"user1"});
        body.put("password", new String[]{"password1"});
        body.put("name", new String[]{"User One"});
        MemberCreateRequestDto memberDto = new MemberCreateRequestDto(body);

        memberRepository.save(connection, memberDto);

        HttpServletRequest request = new CustomHttpServletRequest();
        HttpServletResponse response = new CustomHttpServletResponse();

        ((CustomHttpServletRequest) request).setMethod("GET");

        memberController.doProcess(request, response);

        List<MemberResponseDto> memberList = (List<MemberResponseDto>) request.getAttribute("memberList");
        assertNotNull(memberList);
        assertEquals("/WEB-INF/user/list.jsp", ((CustomHttpServletResponse) response).getForwardedUrl());
    }

    @Test
    public void testDoProcessPost() throws ServletException, IOException {
        HttpServletRequest request = new CustomHttpServletRequest();
        HttpServletResponse response = new CustomHttpServletResponse();

        ((CustomHttpServletRequest) request).setMethod("POST");
        ((CustomHttpServletRequest) request).addParameter("userId", "testUserId");
        ((CustomHttpServletRequest) request).addParameter("password", "password");
        ((CustomHttpServletRequest) request).addParameter("name", "testuserName");

        memberController.doProcess(request, response);

        assertEquals("/api/users", ((CustomHttpServletResponse) response).getRedirectedUrl());

        List<MemberResponseDto> memberList = MemberService.getInstance().getMemberList();
        assertNotNull(memberList);
        assertEquals(1, memberList.size());
        assertEquals("testUserId", memberList.get(0).getUserId());
        assertEquals("testuserName", memberList.get(0).getName());
    }
}