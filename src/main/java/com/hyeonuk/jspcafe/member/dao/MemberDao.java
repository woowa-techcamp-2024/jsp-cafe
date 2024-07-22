package com.hyeonuk.jspcafe.member.dao;

import com.hyeonuk.jspcafe.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDao {
    Member save(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
    void deleteById(String id);
}
