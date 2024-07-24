package com.woowa.cafe.repository.member;

import com.woowa.cafe.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    String save(final Member member);

    Optional<Member> findById(final String memberId);

    List<Member> findAll();

    Optional<Member> update(final Member member);

    void delete(final String memberId);
}
