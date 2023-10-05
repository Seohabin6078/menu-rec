package com.yamukja.member.service;

import com.yamukja.exception.BusinessLogicException;
import com.yamukja.exception.ExceptionCode;
import com.yamukja.member.entity.Member;
import com.yamukja.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
//@Service
public class OAuth2MemberService implements MemberService {
    //todo OAuth2를 이용하는 경우 서비스 계층뿐만 아니라 컨트롤러 계층부터 모든 계층에서 어떻게 회원 데이터를 다룰지에 따라 로직이 변하기 때문에 추후에 정하고 코드 작성하기!
    //todo 이거 OAuth2로 테스트할려면 controller, dto, mapper, db제약조건 등 다 세팅해야 에러가 뜨지 않음
    //todo 지금은 그냥 jwt로 끼워놓고 다음에 이 부분 추가 기능 구현하기!
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        if (!isExistsEmail(member.getEmail())) {
            return memberRepository.save(member);
        }

        return member;
    }

    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());

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

    private boolean isExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.isPresent();
    }
}
