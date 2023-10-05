package com.yamukja.member.service;

import com.yamukja.exception.BusinessLogicException;
import com.yamukja.exception.ExceptionCode;
import com.yamukja.member.entity.Member;
import com.yamukja.member.repository.MemberRepository;
import com.yamukja.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
//@Service
public class JwtMemberService implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.changePassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.createRoles(roles);

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.changePassword(passwordEncoder.encode(password)));
        Optional.ofNullable(member.getDisplayName())
                .ifPresent(findMember::changeDisplayName);
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(findMember::changeMemberStatus);

        return memberRepository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if (member.getMemberStatus() == Member.MemberStatus.MEMBER_SLEEP) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_STATUS_SLEEP);
        } else if (member.getMemberStatus() == Member.MemberStatus.MEMBER_QUIT) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_STATUS_QUIT);
        }
        return member;
    }

    // 최근 가입한 순서대로 모든 회원을 반환
    public List<Member> findAllMembersOrderByCreatedAtDesc() {
        return memberRepository.findAllByOrderByCreatedAtDesc();
    }

    // soft delete 적용
    public Member deleteMember(Long memberId) {
        Member member = findMember(memberId);
        member.changeMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        return memberRepository.save(member);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
