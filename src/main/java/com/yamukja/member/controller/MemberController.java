package com.yamukja.member.controller;

import com.yamukja.member.dto.MemberDto;
import com.yamukja.member.entity.Member;
import com.yamukja.member.mapper.MemberMapper;
import com.yamukja.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/members")
@Validated
@RequiredArgsConstructor // final이나 @notnull이 붙은 필드의 생성자를 자동으로 생성해준다.
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    @PostMapping
    public ResponseEntity<MemberDto.Response> postMember(@RequestBody @Valid MemberDto.Post requestBody) {

        Member member = memberService.createMember(mapper.memberPostDtoToMember(requestBody));
        MemberDto.Response response = mapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<MemberDto.Response> patchMember(@RequestBody @Valid MemberDto.Patch requestBody,
                                                          @PathVariable("member-id") @Positive Long memberId) { // @Positive가 정상작동하기 위해서는 @Validated 애너테이션이 있어야 한다.
        requestBody.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchDtoToMember(requestBody));
        MemberDto.Response response = mapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<MemberDto.Response> getMember(@PathVariable("member-id") @Positive Long memberId) {
        Member member = memberService.findMember(memberId);
        MemberDto.Response response = mapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<MemberDto.Response> deleteMember(@PathVariable("member-id") @Positive Long memberId) {
        Member member = memberService.deleteMember(memberId);
        MemberDto.Response response = mapper.memberToMemberResponseDto(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
