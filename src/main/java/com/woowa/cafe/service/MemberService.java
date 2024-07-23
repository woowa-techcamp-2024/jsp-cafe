package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.SaveMemberDto;
import com.woowa.cafe.repository.user.MemberRepository;

import java.util.List;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String save(final SaveMemberDto saveMember) {
        return memberRepository.save(saveMember.toMember());
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
