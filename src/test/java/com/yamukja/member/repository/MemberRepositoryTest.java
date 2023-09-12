package com.yamukja.member.repository;

import com.yamukja.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원저장소_저장_테스트() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("1234")
                .displayName("홍길동")
                .build();

        //when
        memberRepository.save(member);

        //then
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.get(0).getEmail()).isEqualTo(member.getEmail());
        assertThat(memberList.get(0).getPassword()).isEqualTo(member.getPassword());
        assertThat(memberList.get(0).getDisplayName()).isEqualTo(member.getDisplayName());
    }

    @Test
    void 회원저장소_이메일조회_테스트() {
        //given
        Member member1 = Member.builder()
                .email("hgd@gmail.com")
                .password("1234")
                .displayName("홍길동")
                .build();

        Member member2 = Member.builder()
                .email("hgd123@gmail.com")
                .password("12345678")
                .displayName("홍길동123")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findByEmail(member1.getEmail()).get();
        Member findMember2 = memberRepository.findByEmail(member2.getEmail()).get();

        //then
        assertThat(findMember1.getEmail()).isEqualTo(member1.getEmail());
        assertThat(findMember2.getEmail()).isEqualTo(member2.getEmail());
    }

    @Test
    void 회원저장소_최신순_조회_테스트() {
        //given
        Member member1 = Member.builder()
                .email("hgd1@gmail.com")
                .password("1234")
                .displayName("홍길동1")
                .build();

        Member member2 = Member.builder()
                .email("hgd2@gmail.com")
                .password("1234")
                .displayName("홍길동2")
                .build();

        Member member3 = Member.builder()
                .email("hgd3@gmail.com")
                .password("1234")
                .displayName("홍길동3")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        List<Member> memberList = memberRepository.findAllByOrderByCreatedAtDesc();

        //then
        assertThat(memberList.get(0).getDisplayName()).isEqualTo(member3.getDisplayName());
        assertThat(memberList.get(1).getDisplayName()).isEqualTo(member2.getDisplayName());
        assertThat(memberList.get(2).getDisplayName()).isEqualTo(member1.getDisplayName());
    }
}
