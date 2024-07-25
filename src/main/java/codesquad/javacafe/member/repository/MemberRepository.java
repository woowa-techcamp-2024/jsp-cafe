package codesquad.javacafe.member.repository;


import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
import codesquad.javacafe.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(MemberRepository.class);
    private static final Map<String, Member> map = new ConcurrentHashMap<>();
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }


    public void save(MemberCreateRequestDto memberDto) {
        var member = memberDto.toEntity();
        map.putIfAbsent(member.getUserId(), member);
        System.out.println(map);
    }

    public List<Member> findAll() {
        return map.values().stream().toList();
    }

    public Member findByUserId(String userId) {
        return map.get(userId);
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
