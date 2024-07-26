package com.hyeonuk.jspcafe.member.dao;

import com.hyeonuk.jspcafe.global.db.mysql.MysqlManager;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import com.hyeonuk.jspcafe.member.domain.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlMemberDao implements MemberDao {
    private final MysqlManager manager;
    public MysqlMemberDao(MysqlManager manager) {
        this.manager = manager;
    }

    @Override
    public Member save(Member member) {
        try(Connection conn = manager.getConnection()) {
            if(member.getId() == null){//save작업
                String memberId = member.getMemberId();
                String email = member.getEmail();
                String password = member.getPassword();
                String nickname = member.getNickname();

                String query = "insert into member(memberId,email,password,nickname) values(?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, memberId);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.setString(4, nickname);
                int num = pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()){
                    member.setId(rs.getLong(1));
                }
            }
            else{//update 작업
                Long id = member.getId();
                String email = member.getEmail();
                String password = member.getPassword();
                String nickname = member.getNickname();
                Optional<Member> byId = findById(id);
                if(byId.isPresent()) {
                    String query = "update member set email = ? , password = ? , nickname = ? where id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, email);
                    pstmt.setString(2, password);
                    pstmt.setString(3, nickname);
                    pstmt.setLong(4, id);

                    pstmt.executeUpdate();
                }
                else{
                    String memberId = member.getMemberId();
                    String query = "insert into member(id,memberId,email,password,nickname) values(?,?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setLong(1,id);
                    pstmt.setString(2, memberId);
                    pstmt.setString(3, email);
                    pstmt.setString(4, password);
                    pstmt.setString(5, nickname);

                    pstmt.executeUpdate();
                }
            }
            return member;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        try(Connection conn = manager.getConnection()){
            String sql = "select * from member where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            Member member = null;
            if(rs.next()) {
                member = mappingMember(rs);
            }
            return Optional.ofNullable(member);
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public List<Member> findAll() {
        try(Connection conn = manager.getConnection()){
            String query = "select * from member";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                members.add(mappingMember(rs));
            }
            return members;
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Connection conn = manager.getConnection()){
            String query = "delete from member where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1,id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        try(Connection conn = manager.getConnection()){
            String sql = "select * from member where memberId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            ResultSet rs = pstmt.executeQuery();
            Member member = null;
            if(rs.next()) {
                member = mappingMember(rs);
            }
            return Optional.ofNullable(member);
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    private Member mappingMember(ResultSet rs) throws SQLException {
        String memberId = rs.getString("memberId");
        String nickname = rs.getString("nickname");
        String password = rs.getString("password");
        String email = rs.getString("email");
        long id = rs.getLong("id");
        return new Member(id,memberId, password, nickname, email);
    }
}
