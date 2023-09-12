package com.yamukja.member.service;

import com.yamukja.member.entity.Member;
import com.yamukja.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 중복회원_가입방지_테스트() {
        //given
        given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(new Member()));

        //when //then
        assertThrows(RuntimeException.class, () -> memberService.createMember(new Member()));
    }

    @Test
    void 탈퇴회원_조회_테스트() {
        //given
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .password("1234")
                .displayName("홍길동")
                .build();
        member.changeMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        given(memberRepository.findById(Mockito.anyLong())).willReturn(Optional.of(member));

        //when //then
        assertThrows(RuntimeException.class, () -> memberService.findMember(1L));
    }
}
