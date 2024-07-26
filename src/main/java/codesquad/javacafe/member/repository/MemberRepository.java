package codesquad.javacafe.member.repository;


import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
import codesquad.javacafe.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static codesquad.javacafe.common.db.MySqlConnection.close;
import static codesquad.javacafe.common.db.MySqlConnection.getConnection;

public class MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(MemberRepository.class);
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }


    public void save(Connection connection, MemberCreateRequestDto memberDto) {
        var member = memberDto.toEntity();

        var sql = "insert into member(member_id, member_password, member_name)\n" +
                "select ?,?,? from member\n" +
                "where not exists (select * from member where member_id = ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            if (connection != null) {
                con = connection;
            } else {
                con = getConnection();
            }
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getUserId());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getUserId());
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            long pk = 0;
            if (rs.next()) {
                pk = rs.getLong(1);
            }

            log.debug("[Member Save] pk = {}",pk);

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when member save, Class Info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        }finally {
            if (connection == null) {
                close(con, ps, null);
            } else {
                close(null, ps, null);
            }
        }
    }

    public List<Member> findAll() {
        var sql = " select * from member";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            if (rs.next()) {
                var memberList = new ArrayList<Member>();
                do{
                    Member member = new Member();
                    member.setId(rs.getLong("id"));
                    member.setUserId(rs.getString("member_id"));
                    member.setPassword(rs.getString("member_password"));
                    member.setName(rs.getString("member_name"));
                    memberList.add(member);
                }while(rs.next());

                return memberList;
            } else {
                log.info("[MemberRepository] 사용자 정보가 없습니다.");
                return null;
            }

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when findById, Class info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        }finally {
            close(con,ps,rs);
        }
    }

    public Member findByUserId(Connection connection, String userId) {
        var sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (connection != null) {
                con = connection;
            } else {
                con = getConnection();
            }
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);

            rs = ps.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setUserId(rs.getString("member_id"));
                member.setPassword(rs.getString("member_password"));
                member.setName(rs.getString("member_name"));

                return member;
            } else {
                log.info("[MemberRepository] 사용자 정보가 없습니다.");
                return null;
            }

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when findById, Class info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        }finally {
            if (connection == null) {
                close(con, ps, null);
            } else {
                close(null, ps, null);
            }
        }
    }

    public void update(MemberUpdateRequestDto memberDto) {
        var sql = "update member set member_password = ?, member_name = ?\n" +
                "where member_id = ? and member_password = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, memberDto.getPassword());
            ps.setString(2, memberDto.getName());
            ps.setString(3, memberDto.getUserId());
            ps.setString(4, memberDto.getOldPassword());

            int result = ps.executeUpdate();

            if (result == 0) {
                //TODO error 처리
                log.error("[MemberRepository] update, 비밀번호가 일치하지 않습니다");
            }

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when findById, Class info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        }finally {
            close(con, ps, null);
        }
    }
}
