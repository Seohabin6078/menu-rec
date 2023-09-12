package com.yamukja.member.dto;

import com.yamukja.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class MemberDto {
    @Builder
    @Getter
    public static class Post {
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        @Email(message = "올바른 형식의 이메일 주소여야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+=-])[a-zA-Z\\d~!@#$%^&*()_+=-]{8,24}$",
                message = "비밀번호는 영문(대소문자), 숫자, 특수문자(~!@#$%^&*()_+=-)를 필수항목으로 포함하여 8자리 이상 24자리 이하여야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        @Pattern(regexp = "^[a-zA-Z가-힣][a-zA-Z가-힣\\d]{1,9}$",
                message = "닉네임은 한글 또는 영문으로 시작해야 하며 한글, 영문, 숫자를 조합하여 2자리 이상 10자리 이하여야 합니다.")
        private String displayName;
    }

    @Builder
    @Getter
    public static class Patch {
        private Long memberId;

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+=-])[a-zA-Z\\d~!@#$%^&*()_+=-]{8,24}$",
                message = "비밀번호는 영문(대소문자), 숫자, 특수문자(~!@#$%^&*()_+=-)를 필수항목으로 포함하여 8자리 이상 24자리 이하여야 합니다.")
        private String password;

        @Pattern(regexp = "^[a-zA-Z가-힣][a-zA-Z가-힣\\d]{1,9}$",
                message = "닉네임은 한글 또는 영문으로 시작해야 하며 한글, 영문, 숫자를 조합하여 2자리 이상 10자리 이하여야 합니다.")
        private String displayName;

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }
    }

    @Builder
    @Getter
    public static class Response {
        private Long memberId;
        private String email;
        private String displayName;
        private String memberStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
