package com.yamukja.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {

    @Test
    void 비밀번호_변경_테스트() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        String changePassword = "testtest1234!";

        //when
        member.changePassword(changePassword);

        //then
        assertThat(member.getPassword()).isEqualTo(changePassword);
    }

    @Test
    void 닉네임_변경_테스트() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        String changeDisplayName = "이몽룡";

        //when
        member.changeDisplayName(changeDisplayName);

        //then
        assertThat(member.getDisplayName()).isEqualTo(changeDisplayName);
    }

    @Test
    void 회원상태_변경_테스트() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        Member.MemberStatus changeMemberStatus = Member.MemberStatus.MEMBER_SLEEP;

        //when
        member.changeMemberStatus(changeMemberStatus);

        //then
        assertThat(member.getMemberStatus().getStatus()).isEqualTo("휴면 상태");
    }
}
