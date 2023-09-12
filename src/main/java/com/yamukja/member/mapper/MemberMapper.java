package com.yamukja.member.mapper;

import com.yamukja.member.dto.MemberDto;
import com.yamukja.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public Member memberPostDtoToMember(MemberDto.Post post) {
        if (post == null) {
            return null;
        }

        return Member.builder()
                .email(post.getEmail())
                .password(post.getPassword())
                .displayName(post.getDisplayName())
                .build();
    }

    public Member memberPatchDtoToMember(MemberDto.Patch patch) {
        if (patch == null) {
            return null;
        }

        return Member.builder()
                .memberId(patch.getMemberId())
                .password(patch.getPassword())
                .displayName(patch.getDisplayName())
                .build();
    }

    public MemberDto.Response memberToMemberResponseDto(Member member) {
        if (member == null) {
            return null;
        }

        return MemberDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .displayName(member.getDisplayName())
                .memberStatus(member.getMemberStatus().getStatus())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
