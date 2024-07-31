package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.member.SaveMemberDto;
import com.woowa.cafe.dto.member.UpdateMemberDto;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.repository.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;


public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String save(final SaveMemberDto saveMember) {
        try {
            return memberRepository.save(saveMember.toMember());
        } catch (IllegalArgumentException e) {
            throw new HttpException(SC_BAD_REQUEST, "이미 존재하는 회원입니다.");
        }
    }

    public Member findById(final String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public void update(final String memberId, final UpdateMemberDto updateMemberDto) {
        Member member = findById(memberId);

        if (!member.matchPassword(updateMemberDto.lastPassword())) {
            throw new HttpException(SC_BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        memberRepository.update(updateMemberDto.toMember(memberId))
                .orElseThrow(() -> new HttpException(SC_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public String login(final String memberId, final String password) {
        Member member = findById(memberId);

        if (!member.matchPassword(password)) {
            throw new HttpException(SC_BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return member.getMemberId();
    }
}