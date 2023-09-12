package com.yamukja.member.controller;

import com.google.gson.Gson;
import com.yamukja.member.dto.MemberDto;
import com.yamukja.member.entity.Member;
import com.yamukja.member.mapper.MemberMapper;
import com.yamukja.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
// 아래 방법 이외에 Application 파일에 있는 @EnableJpaAuditing 애너테이션을 따로 @Configuration 클래스에 두는 방법도 있다.
@MockBean(JpaMetamodelMappingContext.class) // @WebMvcTest에는 Jpa 생성과 관련된 기능이 전혀 없기 때문에 JPA Auditing 기능을 사용할 때 필요하다
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean  // 컨트롤러에서 사용하는 서비스가 @WebMvcTest로 인해 등록되지 않았기 때문에 @MockBean을 이용하여 의존성 대체
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Test
    void 컨트롤러_회원가입_테스트() throws Exception {
        //given
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd@gmail.com")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String json = gson.toJson(post);

        MemberDto.Response response =
                MemberDto.Response.builder()
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(mapper.memberPostDtoToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()));
    }

    @Test
    void 컨트롤러_회원수정_테스트() throws Exception {
        //given
        MemberDto.Patch patch =
                MemberDto.Patch.builder()
                        .memberId(1L)
                        .displayName("홍길동")
                        .password("test1234!")
                        .build();
        String json = gson.toJson(patch);

        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .displayName("홍길동")
                        .build();

        given(mapper.memberPatchDtoToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value(patch.getDisplayName()));
    }

    @Test
    void 컨트롤러_회원_한명_조회_테스트() throws Exception {
        //given
        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                get("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.displayName").value(response.getDisplayName()));
    }

    @Test
    void 컨트롤러_회원삭제_테스트() throws Exception {
        //given
        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(memberService.deleteMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                delete("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.displayName").value(response.getDisplayName()));
    }
}
