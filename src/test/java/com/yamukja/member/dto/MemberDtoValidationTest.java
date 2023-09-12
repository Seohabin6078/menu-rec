package com.yamukja.member.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class MemberDtoValidationTest {
    // @BeforeAll 애너테이션이 붙으면 그 메서드는 static이어야 한다.
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 전체_성공_테스트() {
        //given
        MemberDto.Post post = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        //when
        Set<ConstraintViolation<MemberDto.Post>> violations = validator.validate(post);

        //then
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test // @Email은 @만 포함되어 있으면 통과임.
    void 이메일_실패_테스트() {
        //given
        MemberDto.Post post1 = MemberDto.Post.builder()
                .email("hgd")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        MemberDto.Post post2 = MemberDto.Post.builder()
                .email("hgdgmail.com")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        MemberDto.Post post3 = MemberDto.Post.builder()
                .email("")
                .password("test1234!")
                .displayName("홍길동")
                .build();

        String expectMessage = "올바른 형식의 이메일 주소여야 합니다.";

        //when
        Set<ConstraintViolation<MemberDto.Post>> violations1 = validator.validate(post1);
        Set<ConstraintViolation<MemberDto.Post>> violations2 = validator.validate(post2);
        Set<ConstraintViolation<MemberDto.Post>> violations3 = validator.validate(post3);

        //then
        assertThat(violations1.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo(expectMessage);
        // 왜 여기는 NotBlank만 걸리고 @Email에는 안걸리는지 모르겠다.
        assertThat(violations3.iterator().next().getMessage()).isEqualTo("이메일은 공백일 수 없습니다.");
    }

    @Test
    void 비밀번호_실패_테스트() {
        //given
        MemberDto.Post post1 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test")
                .displayName("홍길동")
                .build();

        MemberDto.Post post2 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234")
                .displayName("홍길동")
                .build();

        MemberDto.Post post3 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test12!")
                .displayName("홍길동")
                .build();

        MemberDto.Post post4 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!1234123412341234123412341234123412341234")
                .displayName("홍길동")
                .build();

        MemberDto.Post post5 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("")
                .displayName("홍길동")
                .build();

        String expectMessage = "비밀번호는 영문(대소문자), 숫자, 특수문자(~!@#$%^&*()_+=-)를 필수항목으로 포함하여 8자리 이상 24자리 이하여야 합니다.";

        //when
        Set<ConstraintViolation<MemberDto.Post>> violations1 = validator.validate(post1);
        Set<ConstraintViolation<MemberDto.Post>> violations2 = validator.validate(post2);
        Set<ConstraintViolation<MemberDto.Post>> violations3 = validator.validate(post3);
        Set<ConstraintViolation<MemberDto.Post>> violations4 = validator.validate(post4);
        Set<ConstraintViolation<MemberDto.Post>> violations5 = validator.validate(post5);

        //then
        assertThat(violations1.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations3.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations4.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations5.size()).isEqualTo(2);
    }

    @Test
    void 닉네임_실패_테스트() {
        //given
        MemberDto.Post post1 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍")
                .build();

        MemberDto.Post post2 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("12")
                .build();

        MemberDto.Post post3 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("12홍길동")
                .build();

        MemberDto.Post post4 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("홍길동12341234")
                .build();

        MemberDto.Post post5 = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("test1234!")
                .displayName("")
                .build();

        String expectMessage = "닉네임은 한글 또는 영문으로 시작해야 하며 한글, 영문, 숫자를 조합하여 2자리 이상 10자리 이하여야 합니다.";

        //when
        Set<ConstraintViolation<MemberDto.Post>> violations1 = validator.validate(post1);
        Set<ConstraintViolation<MemberDto.Post>> violations2 = validator.validate(post2);
        Set<ConstraintViolation<MemberDto.Post>> violations3 = validator.validate(post3);
        Set<ConstraintViolation<MemberDto.Post>> violations4 = validator.validate(post4);
        Set<ConstraintViolation<MemberDto.Post>> violations5 = validator.validate(post5);

        //then
        assertThat(violations1.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations3.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations4.iterator().next().getMessage()).isEqualTo(expectMessage);
        assertThat(violations5.size()).isEqualTo(2);
    }
}
