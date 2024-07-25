package codesquad.javacafe.member.service;

import codesquad.javacafe.common.db.MySqlConnection;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
import codesquad.javacafe.member.dto.response.MemberResponseDto;
import codesquad.javacafe.member.entity.Member;
import codesquad.javacafe.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private static final MemberService instance = new MemberService();


    public static MemberService getInstance() {
        return instance;
    }

    public void createMember(MemberCreateRequestDto memberDto) {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            connection.setAutoCommit(false);
            var findMember = MemberRepository.getInstance().findByUserId(connection,memberDto.getUserId());
            if (findMember != null) {
                // TODO error 처리
                log.error("[MemberService] duplicated Member, memberId = {}",findMember.getId());
            }
            MemberRepository.getInstance().save(connection,memberDto);
            connection.commit();

        } catch (SQLException exception) {
            log.error("[SQLException] MemberService createMember, error = {}",exception);
            throw new RuntimeException(exception);
        }finally {
            MySqlConnection.close(connection,null,null);
        }

    }

    public List<MemberResponseDto> getMemberList() {
        var memberList = MemberRepository.getInstance().findAll();
        return memberList.stream()
                .map(member -> new MemberResponseDto(member))
                .collect(Collectors.toList());
    }

    public MemberResponseDto getMemberInfo(String userId) {
        return new MemberResponseDto(MemberRepository.getInstance().findByUserId(null,userId));
    }

    public void updateMember(MemberUpdateRequestDto memberDto) {
        MemberRepository.getInstance().update(memberDto);
    }
}
