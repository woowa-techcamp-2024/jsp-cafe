package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.SaveMemberDto;
import com.woowa.cafe.dto.UpdateMemberDto;
import com.woowa.cafe.repository.user.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String save(final SaveMemberDto saveMember) {
        return memberRepository.save(saveMember.toMember());
    }

    public Member findById(final String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public void update(final String memberId, final UpdateMemberDto updateMemberDto) {
        Member member = findById(memberId);

        if (!member.matchPassword(updateMemberDto.lastPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.update(updateMemberDto.toMember(memberId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
