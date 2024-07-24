package com.woowa.cafe.service;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.dto.SaveMemberDto;
import com.woowa.cafe.dto.UpdateMemberDto;
import com.woowa.cafe.repository.user.InMemoryMemberRepository;
import com.woowa.cafe.repository.user.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    @DisplayName("회원 목록을 가져온다.")
    void findAll() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");
        SaveMemberDto dto1 = new SaveMemberDto("test1", "test1",
                "test1", "test1@test.com");

        memberService.save(dto);
        memberService.save(dto1);

        List<Member> members = memberService.findAll();

        assertAll(
                () -> assertThat(members).hasSize(2),
                () -> assertThat(members.get(0).getMemberId()).isEqualTo(dto.memberId()),
                () -> assertThat(members.get(1).getMemberId()).isEqualTo(dto1.memberId())
        );
    }

    @Test
    @DisplayName("회원을 조회한다 - 단건")
    void findById() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");
        memberService.save(dto);

        memberService.findById(dto.memberId());

        assertThat(memberService.findById(dto.memberId()).getMemberId()).isEqualTo(dto.memberId());
    }

    @Test
    @DisplayName("회원을 조회할 수 없을 경우 에러를 반환한다. - 단건")
    void findById_fail() {
        assertThatThrownBy(() -> memberService.findById("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("회원을 수정한다.")
    void update() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");
        memberService.save(dto);

        UpdateMemberDto updateMemberDto = new UpdateMemberDto("test", "update", "update", "update@update.com");
        memberService.update(dto.memberId(), updateMemberDto);

        assertAll(() -> {
            assertThat(memberService.findById(dto.memberId()).getMemberId()).isEqualTo(dto.memberId());
            assertThat(memberService.findById(dto.memberId()).getName()).isEqualTo(updateMemberDto.name());
            assertThat(memberService.findById(dto.memberId()).toString().contains(updateMemberDto.newPassword())).isTrue();
            assertThat(memberService.findById(dto.memberId()).getEmail()).isEqualTo(updateMemberDto.email());
        });
    }

    @Test
    @DisplayName("회원을 수정한다. - 기존 비밀번호가 일치하지 않으면 예외를 던진다.")
    void update_un_match_password() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");
        memberService.save(dto);

        UpdateMemberDto updateMemberDto = new UpdateMemberDto("unMatch", "update", "update", "update@update.com");
        assertThatThrownBy(() -> memberService.update(dto.memberId(), updateMemberDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원을 수정한다. - 멤버 아이디를 찾을 수 없으면 예외를 던진다.")
    void update_not_found() {
        SaveMemberDto dto = new SaveMemberDto("test", "test",
                "test", "test@test.com");
        memberService.save(dto);

        UpdateMemberDto updateMemberDto = new UpdateMemberDto("test", "update", "update", "update@update.com");
        assertThatThrownBy(() -> memberService.update("not_found", updateMemberDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
