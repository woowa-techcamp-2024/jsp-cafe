package com.woowa.cafe.repository.member;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.utils.DBContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberRepositoryTest {

    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = DBContainer.getDataSource();
        memberRepository = new JdbcMemberRepository(dataSource);

        String dropMemberTable = "DROP TABLE IF EXISTS members";
        String createMemberTable = "CREATE TABLE members (" +
                "member_id VARCHAR(255) PRIMARY KEY, " +
                "password VARCHAR(255), " +
                "name VARCHAR(255), " +
                "email VARCHAR(255))";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(dropMemberTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(createMemberTable);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("회원을 생성할 수 있다.")
    void create() {
        // given
        Member member = new Member("memberId", "password", "name", "email");

        // when
        memberRepository.save(member);

        // then
        Member savedMember = memberRepository.findById("memberId").get();

        assertAll(
                () -> assertEquals(member.getMemberId(), savedMember.getMemberId()),
                () -> assertEquals(member.getPassword(), savedMember.getPassword()),
                () -> assertEquals(member.getName(), savedMember.getName()),
                () -> assertEquals(member.getEmail(), savedMember.getEmail())
        );
    }

    @Test
    @DisplayName("회원을 수정할 수 있다.")
    void update() {
        // given
        Member member = new Member("memberId", "password", "name", "email");
        memberRepository.save(member);

        // when
        Member updatedMember = new Member("memberId", "newPassword", "newName", "newEmail");
        memberRepository.update(updatedMember);

        // then
        Member savedMember = memberRepository.findById("memberId").get();

        assertAll(
                () -> assertEquals(updatedMember.getMemberId(), savedMember.getMemberId()),
                () -> assertEquals(updatedMember.getPassword(), savedMember.getPassword()),
                () -> assertEquals(updatedMember.getName(), savedMember.getName()),
                () -> assertEquals(updatedMember.getEmail(), savedMember.getEmail())
        );
    }

    @Test
    @DisplayName("회원 아이디들을 바탕으로 조회할 수 있다.")
    void findByIds() {
        // given
        Member member1 = new Member("memberId1", "password1", "name1", "email1@1.com");
        Member member2 = new Member("memberId2", "password2", "name2", "email2@1.com");
        Member member3 = new Member("memberId3", "password3", "name3", "email3@1.com");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        var members = memberRepository.findMembersByIds(List.of("memberId1", "memberId2", "memberId3"));

        // then
        assertAll(
                () -> assertEquals(3, members.size()),
                () -> assertEquals(member1, members.get(0)),
                () -> assertEquals(member2, members.get(1)),
                () -> assertEquals(member3, members.get(2))
        );
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다.")
    void findAll() {
        // given
        Member member1 = new Member("memberId1", "password1", "name1", "email1@1.com");
        Member member2 = new Member("memberId2", "password2", "name2", "email2@1.com");
        Member member3 = new Member("memberId3", "password3", "name3", "email3@1.com");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        var members = memberRepository.findAll();

        // then
        assertAll(
                () -> assertEquals(3, members.size()),
                () -> assertEquals(member1, members.get(0)),
                () -> assertEquals(member2, members.get(1)),
                () -> assertEquals(member3, members.get(2))
        );
    }

    @Test
    @DisplayName("회원 아이디로 회원을 찾는다.")
    void findById() {
        Member member1 = new Member("memberId1", "password1", "name1", "email1@1.com");

        memberRepository.save(member1);

        Member foundMember = memberRepository.findById("memberId1").get();

        assertEquals(member1, foundMember);
    }
}
