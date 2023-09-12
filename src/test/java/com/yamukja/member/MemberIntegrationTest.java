package com.yamukja.member;

import com.google.gson.Gson;
import com.yamukja.member.dto.MemberDto;
import com.yamukja.member.entity.Member;
import com.yamukja.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc // 이게 없으면 MockMvc를 autowired 할 수 없다. @WebMvcTest에 해당 애너테이션이 포함되어 있음.
@SpringBootTest
public class MemberIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private MemberRepository memberRepository;

    private MemberDto.Post post;

    private ResultActions postResultActions;

    private Long memberId;

    @BeforeEach
    void init() throws Exception {
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd@gmail.com")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String postJson = gson.toJson(post);
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
        );
        this.post = post;
        this.postResultActions = actions;
        this.memberId = memberRepository.findAll().get(0).getMemberId();
    }

    @Test
    void 회원가입_통합테스트() throws Exception {
        //given when
        //init()

        //then
        postResultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()))
                .andExpect(jsonPath("$.memberStatus").value(Member.MemberStatus.MEMBER_ACTIVE.getStatus()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty());
    }

    @Test
    void 회원수정_통합테스트() throws Exception {
        //given


        MemberDto.Patch patch =
                MemberDto.Patch.builder()
                        .memberId(memberId)
                        .displayName("이몽룡")
                        .password("test12341234!")
                        .build();
        String patchJson = gson.toJson(patch);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members/" + memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(patch.getDisplayName()))
                .andExpect(jsonPath("$.memberStatus").value(Member.MemberStatus.MEMBER_ACTIVE.getStatus()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty());
    }

    @Test
    void 회원_한명_조회_통합테스트() throws Exception {
        //given
        //init()

        //when
        ResultActions actions = mockMvc.perform(
                get("/members/" + memberId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()))
                .andExpect(jsonPath("$.memberStatus").value(Member.MemberStatus.MEMBER_ACTIVE.getStatus()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty());
    }

    @Test
    void 회원삭제_통합테스트() throws Exception {
        //given
        //init()

        //when
        ResultActions actions = mockMvc.perform(
                delete("/members/" + memberId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()))
                .andExpect(jsonPath("$.memberStatus").value(Member.MemberStatus.MEMBER_QUIT.getStatus()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty());
    }
}
