package com.woowa.cafe.repository.member;

import com.woowa.cafe.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcMemberRepository.class);
    private final DataSource dataSource;

    public JdbcMemberRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String save(final Member member) {
        try (var connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO member VALUES (?, ?, ?, ?)");
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getEmail());
            pstmt.executeUpdate();

            return member.getMemberId();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Member> findById(final String memberId) {
        try (var connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM member WHERE member_id = ?");
            pstmt.setString(1, memberId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Member(rs.getString("member_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
            }

            return Optional.empty();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Member> findMembersByIds(final List<String> memberIds) {
        if (memberIds.isEmpty()) {
            return List.of();
        }
        try (var connection = dataSource.getConnection()) {
            StringBuilder sb = new StringBuilder("SELECT * FROM member WHERE member_id IN (");
            for (int i = 0; i < memberIds.size(); i++) {
                sb.append("?");
                if (i < memberIds.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            PreparedStatement pstmt = connection.prepareStatement(sb.toString());
            for (int i = 0; i < memberIds.size(); i++) {
                pstmt.setString(i + 1, memberIds.get(i));
            }

            var rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(new Member(rs.getString("member_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
            return members;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();

        try (var connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM member");
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                members.add(new Member(rs.getString("member_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
            }

            return members;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Member> update(final Member member) {
        try (var connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE member SET password = ?, name = ?, email = ? WHERE member_id = ?");
            pstmt.setString(1, member.getPassword());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getMemberId());
            pstmt.executeUpdate();

            return Optional.of(member);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(final String memberId) {

    }
}
