package com.hyeonuk.jspcafe.member.dao;

import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import com.hyeonuk.jspcafe.member.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemberDao implements MemberDao{
    private final Map<Long,Member> store = new ConcurrentHashMap<>();
    private final static AtomicLong idGenerator = new AtomicLong(1l);

    @Override
    public Member save(Member member) {
        if(!member.validation()){
            throw new DataIntegrityViolationException("can't persist null or empty value");
        }

        if(member.getId() == null && findByMemberId(member.getMemberId()).isPresent()){
            throw new DataIntegrityViolationException("id is a unique column");
        }

        if(member.getId() == null){
            member.setId(idGenerator.incrementAndGet());
        }

        store.put(member.getId(),member);

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return store.values()
                .stream()
                .filter(member->member.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        findById(id)
                .ifPresent(member->{
                    store.remove(member.getId());
                });
    }

    @Override
    public Optional<Member> findByMemberId(String memberId) {
        return store.values().stream()
                .filter(member->member.getMemberId().equals(memberId))
                .findFirst();
    }
}
