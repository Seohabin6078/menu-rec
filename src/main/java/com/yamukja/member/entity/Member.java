package com.yamukja.member.entity;

import com.yamukja.audit.Auditable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 10)
    private String displayName;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void createRoles(List<String> roles) {
        this.roles = roles;
    }

    @Builder
    public Member(Long memberId, String email, String password, String displayName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void changeMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void updateMember(Long memberId, String email, String password, String displayName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }

    @Getter
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        private final String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
}
