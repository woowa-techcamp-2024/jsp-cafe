package codesquad.javacafe.member.repository;


import codesquad.javacafe.member.entity.Member;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberRepository {
    private static final Map<String, Member> map = new ConcurrentHashMap<>();
    private static final MemberRepository instance = new MemberRepository();
    public static MemberRepository getInstance() {
        return instance;
    }


    public void save(Member member) {
        map.putIfAbsent(member.getUserId(), member);
        System.out.println(map);
    }

    public List<Member> findAll() {
        return map.values().stream().toList();
    }

    public Member findByUserId(String userId) {
        return map.get(userId);
    }
}
