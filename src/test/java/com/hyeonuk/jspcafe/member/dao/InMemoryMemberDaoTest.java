package com.hyeonuk.jspcafe.member.dao;

import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;
import com.hyeonuk.jspcafe.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MemberDao 테스트")
class InMemoryMemberDaoTest {
    MemberDao memberDao = new InMemoryMemberDao();

    private void assertMemberValues(Member expected,Member actual){
        assertAll("member assert",
                ()->assertEquals(expected.getId(),actual.getId()),
                ()->assertEquals(expected.getPassword(),actual.getPassword()),
                ()->assertEquals(expected.getEmail(),actual.getEmail()),
                ()->assertEquals(expected.getNickname(),actual.getNickname())
        );
    }
    @Nested
    @DisplayName("save 메서드는")
    class SaveMemberTest{
        @Nested
        @DisplayName("id값이 null인 요청이 들어왔을 때")
        class NotExistsIdValue{
            @Test
            @DisplayName("제대로 저장이 되고, id를 부여받는다")
            void successMemberSave(){
                //given
                String userId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(userId,password,email,nickname);

                //when
                Member save = memberDao.save(member);

                //then
                assertMemberValues(save,member);
                assertNotNull(save.getId());
            }
        }
        @Nested
        @DisplayName("id값이 존재할 때")
        class ExistsIdValue{
            @Test
            @DisplayName("기존에 id값이 데이터베이스에 없다면 제대로 저장됨")
            void persist(){
                //given
                Long id = Long.valueOf(10l);
                String userId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,userId,password,email,nickname);

                //when
                Member saved = memberDao.save(member);

                //then
                assertMemberValues(member,saved);
            }

            @Test
            @DisplayName("기존에 id값이 데이터베이스에 존재한다면 update됨")
            void update(){
                //given
                Long id = Long.valueOf(10l);
                String userId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,userId,password,email,nickname);
                memberDao.save(member);

                String updatedUserId = "updateId";
                String updatedPassword = "updatedPassword123";
                String updatedEmail = "updatedEmail123@gmail.com";
                Member updatedMember = new Member(id,updatedUserId,updatedPassword,updatedEmail,nickname);

                //when
                Member saved = memberDao.save(updatedMember);
                Optional<Member> byId = memberDao.findById(id);

                //then
                assertMemberValues(updatedMember,saved);
                assertTrue(byId.isPresent());
                Member find = byId.get();
                assertMemberValues(updatedMember,find);
            }
        }

        @Nested
        @DisplayName("아이디가 중복된다면")
        class DuplicateId{
            @Test
            @DisplayName("오류를 던진다.")
            void duplicatedId(){
                //given
                String userId = "rlagusdnr120";
                String password = "password123";
                String nickname = "Hyeon_Uk_2";
                String email = "rlagusdnr120@gmail.com";

                Member member = new Member(userId,password,nickname,email);
                memberDao.save(member);

                Member duplicatedIdMember = new Member(userId,"pw1234","newUser","new@gmail.com");

                //when & then
                assertThrows(DataIntegrityViolationException.class,()->{
                    memberDao.save(duplicatedIdMember);
                });
            }
        }

        @Nested
        @DisplayName("기본 값중 하나라도 존재하지 않는다면")
        class FailMemberWithoutMoreThanOneValue{
            @Test
            @DisplayName("아이디가 존재하지 않으면 오류를 던진다")
            void withoutId(){
                //given
                String userId = null;
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(userId,password,email,nickname);

                //when & then
                assertThrows(DataIntegrityViolationException.class,()->{
                    Member save = memberDao.save(member);
                });
            }

            @Test
            @DisplayName("비밀번호가 존재하지 않으면 오류를 던진다")
            void withoutPassword(){
                //given
                String userId = "rlagusdnr120";
                String password = null;
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(null,password,email,nickname);

                //when & then
                assertThrows(DataIntegrityViolationException.class,()->{
                    Member save = memberDao.save(member);
                });
            }

            @Test
            @DisplayName("닉네임이 존재하지 않으면 오류를 던진다.")
            void successMemberSave(){
                //given
                String userId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = null;
                Member member = new Member(userId,password,email,nickname);

                //when & then
                assertThrows(DataIntegrityViolationException.class,()->{
                    Member save = memberDao.save(member);
                });
            }
        }
    }

    @Nested
    @DisplayName("findByMemberId 메서드는")
    class FindByMemberidTest{
        @Nested
        @DisplayName("값이 존재하면")
        class existsValue{
            @Test
            @DisplayName("값을 Optional로 감싸 반환한다.")
            void getMember(){
                //given
                Long id = 1l;
                String memberId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,memberId,password,nickname,email);

                Member member2 = new Member(2l,"otherId","otherPassword","otherEmail","otherNickname");
                memberDao.save(member);
                memberDao.save(member2);

                //when
                Optional<Member> byMemberId = memberDao.findByMemberId(member.getMemberId());

                //then
                assertTrue(byMemberId.isPresent());
                assertMemberValues(member,byMemberId.get());
            }
        }

        @Nested
        @DisplayName("값이 존재하지 않으면")
        class notExistsValue{
            @Test
            @DisplayName("null값이 optional에 감싸 반환된다")
            void getMember(){
                //given
                Long id = 1l;
                String memberId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,memberId,password,nickname,email);
                memberDao.save(member);

                //when
                Optional<Member> byMemberId = memberDao.findByMemberId("notExists");

                //then
                assertTrue(byMemberId.isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindByIdTest{
        @Nested
        @DisplayName("값이 존재하면")
        class ExistsValue{
            @Test
            @DisplayName("값을 Optional로 감싸 반환한다.")
            void getMember(){
                //given
                Long id = 1l;
                String memberId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,memberId,password,nickname,email);

                Member member2 = new Member(2l,"otherId","otherPassword","otherEmail","otherNickname");
                memberDao.save(member);
                memberDao.save(member2);

                //when
                Optional<Member> byMemberId = memberDao.findById(member.getId());

                //then
                assertTrue(byMemberId.isPresent());
                assertMemberValues(member,byMemberId.get());
            }
        }

        @Nested
        @DisplayName("값이 존재하지 않으면")
        class NotExistsValue{
            @Test
            @DisplayName("null값이 optional에 감싸 반환된다")
            void getMember(){
                //given
                Long id = 1l;
                String memberId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,memberId,password,nickname,email);
                memberDao.save(member);

                //when
                Optional<Member> byMemberId = memberDao.findById(Long.MAX_VALUE);

                //then
                assertTrue(byMemberId.isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteByIdTest{
        @Nested
        @DisplayName("Id값이 존재하면")
        class ExistsIdValue{
            @Test
            @DisplayName("해당 값만 비영속화 된다.")
            void deleteMemberByID(){
                //given
                Long id = 1l;
                String memberId = "rlagusdnr120";
                String password = "password123";
                String email = "rlagusdnr120@gmail.com";
                String nickname = "Hyeon_Uk_2";
                Member member = new Member(id,memberId,password,nickname,email);

                Member member2 = new Member(2l,"otherId","otherPassword","otherEmail","otherNickname");
                memberDao.save(member);
                memberDao.save(member2);

                //when
                memberDao.deleteById(member.getId());

                //then
                assertTrue(memberDao.findById(member.getId()).isEmpty());
                assertTrue(memberDao.findById(member2.getId()).isPresent());
            }
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAllTest{
        @Nested
        @DisplayName("유저가 존재하면")
        class ExistsMember{
            @Test
            @DisplayName("모든 유저의 값을 가져온다.")
            void getAllMembers(){
                //given
                Member member1 = new Member("id1","pw1","nick1","email1");
                Member member2 = new Member("id2","pw2","nick2","email2");
                Member member3 = new Member("id3","pw3","nick3","email3");
                Member member4 = new Member("id4","pw4","nick4","email4");
                Member member5 = new Member("id5","pw5","nick5","email5");
                List<Member> memberList = List.of(member1,member2,member3,member4,member5);
                memberList.forEach(memberDao::save);

                //when
                List<Member> all = memberDao.findAll();

                //then
                assertEquals(memberList.size(),all.size());
                assertTrue(all.containsAll(memberList));
            }
        }

        @Nested
        @DisplayName("유저가 존재하지 않으면")
        class NotExistsMember{
            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void getEmptyMember(){
                //given

                //when
                List<Member> all = memberDao.findAll();

                //then
                assertTrue(all.isEmpty());
            }
        }
    }
}