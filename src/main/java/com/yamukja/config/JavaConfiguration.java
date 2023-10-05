package com.yamukja.config;

import com.yamukja.member.repository.MemberRepository;
import com.yamukja.member.service.JwtMemberService;
import com.yamukja.member.service.MemberService;
import com.yamukja.member.service.OAuth2MemberService;
import com.yamukja.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class JavaConfiguration {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    @Bean
    public MemberService memberService() {
        return new JwtMemberService(memberRepository, passwordEncoder, authorityUtils);
    }
}
