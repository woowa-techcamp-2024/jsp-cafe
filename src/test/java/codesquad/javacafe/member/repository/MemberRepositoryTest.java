package codesquad.javacafe.member.repository;

import codesquad.javacafe.common.db.DBConnection;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
import codesquad.javacafe.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {
    private static MemberRepository memberRepository;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        memberRepository = MemberRepository.getInstance();
        DBConnection.setConnectionInfo("jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        connection = DBConnection.getConnection();
        createTable();
    }

    @AfterEach
    void afterEach() throws SQLException {
        clearTable();
    }

    private static void createTable() throws SQLException {
        var sql = "CREATE TABLE if not exists member (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "member_id VARCHAR(255) NOT NULL, " +
                "member_password VARCHAR(255) NOT NULL, " +
                "member_name VARCHAR(255) NOT NULL" +
                ")";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void clearTable() throws SQLException {
        var sql = "DELETE FROM member";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    void testSave() throws SQLException {
        Map<String, String[]> body = new HashMap<>();
        body.put("userId", new String[]{"user1"});
        body.put("password", new String[]{"password1"});
        body.put("name", new String[]{"User One"});
        MemberCreateRequestDto memberDto = new MemberCreateRequestDto(body);
        var member = memberDto.toEntity();
        memberRepository.save(member);

        var sql = "SELECT * FROM member WHERE member_id = 'user1'";
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {
            assertTrue(resultSet.next());
            assertEquals("user1", resultSet.getString("member_id"));
            assertEquals("password1", resultSet.getString("member_password"));
            assertEquals("User One", resultSet.getString("member_name"));
        }
    }

    @Test
    void testFindAll() throws SQLException {
        Map<String, String[]> body1 = new HashMap<>();
        body1.put("userId", new String[]{"user1"});
        body1.put("password", new String[]{"password1"});
        body1.put("name", new String[]{"User One"});
        MemberCreateRequestDto memberDto1 = new MemberCreateRequestDto(body1);
        var member1 = memberDto1.toEntity();
        Map<String, String[]> body2 = new HashMap<>();
        body2.put("userId", new String[]{"user2"});
        body2.put("password", new String[]{"password2"});
        body2.put("name", new String[]{"User Two"});
        MemberCreateRequestDto memberDto2 = new MemberCreateRequestDto(body2);
        var member2 = memberDto2.toEntity();

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println(member);
        }

        assertNotNull(members);
        assertEquals(2, members.size());
    }

    @Test
    void testFindByUserId() throws SQLException {
        Map<String, String[]> body = new HashMap<>();
        body.put("userId", new String[]{"user1"});
        body.put("password", new String[]{"password1"});
        body.put("name", new String[]{"User One"});
        MemberCreateRequestDto memberDto = new MemberCreateRequestDto(body);
        var member = memberDto.toEntity();
        memberRepository.save(member);

        Member findMember = memberRepository.findByUserId("user1");

        assertNotNull(findMember);
        assertEquals("user1", findMember.getUserId());
        assertEquals("password1", findMember.getPassword());
        assertEquals("User One", findMember.getName());
    }

    @Test
    void testUpdate() throws SQLException {
        Map<String, String[]> body = new HashMap<>();
        body.put("userId", new String[]{"user1"});
        body.put("password", new String[]{"password1"});
        body.put("name", new String[]{"User One"});
        MemberCreateRequestDto memberDto = new MemberCreateRequestDto(body);
        var member = memberDto.toEntity();
        memberRepository.save(member);

        Map<String, String[]> body2 = new HashMap<>();
        body2.put("userId", new String[]{"user1"});
        body2.put("oldPassword", new String[]{"password1"});
        body2.put("password", new String[]{"password2"});
        body2.put("name", new String[]{"User Two"});
        MemberUpdateRequestDto updateDto = new MemberUpdateRequestDto(body2);
        memberRepository.update(updateDto);

        var sql = "SELECT * FROM member WHERE member_id = 'user1'";
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {
            assertTrue(resultSet.next());
            assertEquals("password2", resultSet.getString("member_password"));
            assertEquals("User Two", resultSet.getString("member_name"));
        }
    }
}