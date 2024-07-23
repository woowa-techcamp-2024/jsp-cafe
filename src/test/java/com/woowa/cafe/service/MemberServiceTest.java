package com.woowa.cafe.service;

import com.woowa.cafe.dto.SaveMemberDto;
import com.woowa.cafe.repository.user.InMemoryMemberRepository;
import com.woowa.cafe.repository.user.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest {

    MemberRepository memberRepository;
    MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("회원 가입이 정상적으로 된다.")
    void save() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");

        String save = memberService.save(dto);

        assertThat(save).isEqualTo(dto.memberId());
    }

    @Test
    @DisplayName("중복 아이디는 허용하지 않는다.")
    void save_duplicate_memberId() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");

        memberService.save(dto);

        assertThatThrownBy(() -> memberService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 회원입니다.");
    }

}
