package com.woowa.cafe.repository.user;

import com.woowa.cafe.domain.Member;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMemberRepository implements MemberRepository {

    private final Map<String, Member> members = new ConcurrentHashMap<>();

    @Override
    public String save(final Member member) {
        if (members.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        members.put(member.getMemberId(), member);

        return member.getMemberId();
    }

    @Override
    public Optional<Member> findById(final String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public List<Member> findAll() {
        return members.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Member> update(final Member member) {
        if (!members.containsKey(member.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        members.put(member.getMemberId(), member);

        return Optional.of(member);
    }

    @Override
    public void delete(final String memberId) {
        members.remove(memberId);
    }

}
