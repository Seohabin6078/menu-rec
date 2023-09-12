package com.yamukja.member.mapper;

import com.yamukja.member.dto.MemberDto;
import com.yamukja.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberMapperTest {

    private static MemberMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new MemberMapper();
    }

    @Test
    @DisplayName("postDto에서 엔티티로 변경 테스트")
    void memberPostDtoToMemberTest() {
        //given
        MemberDto.Post post = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        //when
        Member resultMember = mapper.memberPostDtoToMember(post);

        //then
        assertThat(resultMember.getEmail()).isEqualTo(post.getEmail());
        assertThat(resultMember.getPassword()).isEqualTo(post.getPassword());
        assertThat(resultMember.getDisplayName()).isEqualTo(post.getDisplayName());
        assertThat(resultMember.getMemberId()).isNull();
    }

    @Test
    @DisplayName("patchDto에서 엔티티로 변경 테스트")
    void memberPatchDtoToMemberTest() {
        //given
        MemberDto.Patch patch = MemberDto.Patch.builder()
                .memberId(1L)
                .password("test1234!")
                .displayName("홍길동")
                .build();

        //when
        Member resultMember = mapper.memberPatchDtoToMember(patch);

        //then
        assertThat(resultMember.getMemberId()).isEqualTo(patch.getMemberId());
        assertThat(resultMember.getPassword()).isEqualTo(patch.getPassword());
        assertThat(resultMember.getDisplayName()).isEqualTo(patch.getDisplayName());
        assertThat(resultMember.getEmail()).isNull();
    }

    @Test
    @DisplayName("엔티티에서 responseDto로 변경 테스트")
    void memberToMemberResponseDtoTest() {
        //given
        Member member = Member.builder()
                .memberId(1L)
                .email("hgd@gmail.com")
                .displayName("홍길동")
                .build();

        //when
        MemberDto.Response response = mapper.memberToMemberResponseDto(member);

        //then
        assertThat(response.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getDisplayName()).isEqualTo(member.getDisplayName());
        assertThat(response.getCreatedAt()).isNull();
        assertThat(response.getModifiedAt()).isNull();
    }
}
