package codesquad.javacafe.member.repository;


import codesquad.javacafe.common.db.MySqlConnection;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
import codesquad.javacafe.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static codesquad.javacafe.common.db.MySqlConnection.close;
import static codesquad.javacafe.common.db.MySqlConnection.getConnection;

public class MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(MemberRepository.class);
    private static final Map<String, Member> map = new ConcurrentHashMap<>();
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }


    public void save(Connection connection, MemberCreateRequestDto memberDto) {
        var member = memberDto.toEntity();


        var sql = "insert into member(member_id, member_password, member_name)\n" +
                "values(?,?,?)";

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
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            long pk = 0;
            if (rs.next()) {
                pk = rs.getLong(1);
            }

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

        map.putIfAbsent(member.getUserId(), member);
        System.out.println(map);
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
        log.debug("[MemberRepository] memberDto = {}", memberDto);
        var member = map.get(memberDto.getUserId());
        if (Objects.isNull(member)) {
            // TODO error 처리
            log.error("[MemberRepository] member is null");
        }
        log.debug("[UPDATE] find member = {}",member);
        if (!member.isPasswordSame(memberDto.getOldPassword())) {
            // TODO error 처리
            log.error("[MemberRepository] password not same");
        }
        member.update(memberDto.getPassword(), memberDto.getName());
        map.put(member.getUserId(),member);
    }
}
