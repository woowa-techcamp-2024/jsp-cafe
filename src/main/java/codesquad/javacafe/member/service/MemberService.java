package codesquad.javacafe.member.service;

import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.response.MemberResponseDto;
import codesquad.javacafe.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MemberService {
    private static final MemberService instance = new MemberService();
    private static final MemberRepository memberRepository = MemberRepository.getInstance();

    public static MemberService getInstance() {
        return instance;
    }

    public void createMember(MemberCreateRequestDto memberDto) {
        var member = memberDto.toEntity();
        memberRepository.save(member);
    }

    public List<MemberResponseDto> getMemberList() {
        var memberList = memberRepository.findAll();
        return memberList.stream()
                .map(member -> new MemberResponseDto(member))
                .collect(Collectors.toList());
    }
}
